package pe.com.graduate.insights.api.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.SurveyStatisticsUseCase;
import pe.com.graduate.insights.api.application.ports.output.SurveyRepositoryPort;
import pe.com.graduate.insights.api.application.ports.output.GraduateRepositoryPort;
import pe.com.graduate.insights.api.application.ports.output.GraduateSurveyResponseRepositoryPort;
import pe.com.graduate.insights.api.domain.models.response.*;
import pe.com.graduate.insights.api.infrastructure.repository.entities.*;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.*;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.SurveyTypeMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SurveyStatisticsService implements SurveyStatisticsUseCase {
    
    private final SurveyRepositoryPort surveyRepositoryPort;
    private final GraduateRepositoryPort graduateRepositoryPort;
    private final GraduateSurveyResponseRepositoryPort responseRepositoryPort;
    private final SurveyRepository surveyRepository;
    private final GraduateRepository graduateRepository;
    private final GraduateSurveyResponseRepository graduateSurveyResponseRepository;
    private final GraduateQuestionResponseRepository graduateQuestionResponseRepository;
    private final EducationCenterRepository educationCenterRepository;
    private final SurveyTypeMapper surveyTypeMapper;
    
    @Override
    public SurveyStatisticsResponse getSurveyStatistics(Long surveyId) {
        // Obtener datos de la encuesta
        SurveyEntity survey = surveyRepository.findById(surveyId)
            .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));
        
        // Obtener respuestas de la encuesta
        List<GraduateSurveyResponseEntity> surveyResponses = graduateSurveyResponseRepository.findBySurveyId(surveyId);
        List<GraduateSurveyResponseEntity> completedResponses = graduateSurveyResponseRepository.findBySurveyIdAndCompleted(surveyId, true);
        
        // Obtener total de graduados (asumiendo que es el total activo)
        Long totalGraduates = graduateRepository.countByUserEstado("1");
        
        // Calcular métricas
        int totalResponses = surveyResponses.size();
        int completedCount = completedResponses.size();
        double responseRate = totalGraduates > 0 ? (totalResponses * 100.0) / totalGraduates : 0.0;
        double completionRate = totalResponses > 0 ? (completedCount * 100.0) / totalResponses : 0.0;
        
        // Obtener estadísticas por pregunta
        List<QuestionStatistics> questionStats = generateQuestionStatisticsFromDb(surveyId, survey.getQuestions());
        
        // Obtener datos demográficos
        Map<String, Long> responsesByLocation = getResponsesByLocation(surveyId);
        Map<String, Long> responsesByIndustry = getResponsesByIndustry(surveyId);
        Map<String, Long> responsesByGender = getResponsesByGender(surveyId);
        Map<String, Long> responsesByEmploymentStatus = getResponsesByEmploymentStatus(surveyId);
        
        // Obtener datos temporales
        Map<String, Long> responsesByMonth = getResponsesByMonth(surveyId);
        
        return SurveyStatisticsResponse.builder()
            .surveyId(surveyId)
            .surveyTitle(survey.getTitle())
            .surveyDescription(survey.getDescription())
            .surveyType(surveyTypeMapper.toDomain(survey.getSurveyType()))
            .status(survey.getStatus())
            .startDate(survey.getStartDate())
            .endDate(survey.getEndDate())
            .dataGeneratedAt(LocalDateTime.now())
            .totalResponses(totalResponses)
            .totalGraduates(totalGraduates.intValue())
            .pendingResponses(totalGraduates.intValue() - totalResponses)
            .responseRate(responseRate)
            .completionRate(completionRate)
            .totalQuestions(survey.getQuestions() != null ? survey.getQuestions().size() : 0)
            .questionStatistics(questionStats)
            .responsesByLocation(responsesByLocation)
            .responsesByIndustry(responsesByIndustry)
            .responsesByGender(responsesByGender)
            .responsesByEmploymentStatus(responsesByEmploymentStatus)
            .responsesByMonth(responsesByMonth)
            .build();
    }
    
    @Override
    public ChartDataResponse getQuestionChartData(Long surveyId, Long questionId, String chartType) {
        // Obtener la pregunta
        List<GraduateQuestionResponseEntity> responses = graduateQuestionResponseRepository.findBySurveyIdAndQuestionId(surveyId, questionId);
        
        if (responses.isEmpty()) {
            return ChartDataResponse.builder()
                .chartType(chartType)
                .title("Sin datos disponibles")
                .labels(List.of("Sin respuestas"))
                .totalResponses(0L)
                .build();
        }
        
        QuestionEntity question = responses.get(0).getQuestion();
        Long totalResponses = graduateQuestionResponseRepository.countResponsesByQuestionId(questionId);
        
        ChartDataResponse.ChartDataResponseBuilder builder = ChartDataResponse.builder()
            .chartType(chartType)
            .title(question.getQuestionText())
            .totalResponses(totalResponses);
        
        switch (question.getQuestionType()) {
            case SINGLE_CHOICE:
            case MULTIPLE_CHOICE:
            case YES_NO:
                Map<String, Long> optionCounts = getOptionCountsForQuestion(questionId);
                List<String> labels = new ArrayList<>(optionCounts.keySet());
                List<Object> data = labels.stream()
                    .map(optionCounts::get)
                    .collect(Collectors.toList());
                
                builder.labels(labels)
                       .datasets(Arrays.asList(
                           ChartDataResponse.ChartDataset.builder()
                               .label("Respuestas")
                               .data(data)
                               .backgroundColors(generateColors(labels.size()))
                               .build()
                       ));
                break;
                
            case SCALE:
            case NUMBER:
                List<Integer> numericResponses = graduateQuestionResponseRepository.findNumericResponsesByQuestionId(questionId);
                Map<String, Long> distribution = numericResponses.stream()
                    .collect(Collectors.groupingBy(
                        String::valueOf,
                        Collectors.counting()
                    ));
                
                List<String> numLabels = new ArrayList<>(distribution.keySet());
                Collections.sort(numLabels);
                List<Object> numData = numLabels.stream()
                    .map(distribution::get)
                    .collect(Collectors.toList());
                
                builder.labels(numLabels)
                       .datasets(Arrays.asList(
                           ChartDataResponse.ChartDataset.builder()
                               .label("Distribución")
                               .data(numData)
                               .backgroundColor("#2196F3")
                               .build()
                       ));
                break;
                
            case TEXT:
                // Para texto, mostrar palabras más comunes
                List<String> textResponses = graduateQuestionResponseRepository.findTextResponsesByQuestionId(questionId);
                Map<String, Long> wordCount = getWordFrequency(textResponses);
                
                List<String> topWords = wordCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
                
                List<Object> wordData = topWords.stream()
                    .map(wordCount::get)
                    .collect(Collectors.toList());
                
                builder.labels(topWords)
                       .datasets(Arrays.asList(
                           ChartDataResponse.ChartDataset.builder()
                               .label("Frecuencia de palabras")
                               .data(wordData)
                               .backgroundColor("#4CAF50")
                               .build()
                       ));
                break;
        }
        
        builder.configuration(createDefaultChartConfiguration(chartType));
        
        return builder.build();
    }
    
    @Override
    public DashboardOverviewResponse getDashboardOverview(Integer graduationYear, Long educationCenterId) {
        // Obtener estadísticas generales
        Long totalSurveys = surveyRepository.count();
        Long activeSurveys = surveyRepository.findByStatus(SurveyStatus.ACTIVE).stream().count();
        Long totalGraduates = graduateRepository.countByUserEstado("1");
        
        // Obtener total de respuestas (asumiendo respuestas únicas por graduado)
        Long totalResponses = graduateSurveyResponseRepository.count();
        Double overallResponseRate = totalGraduates > 0 ? (totalResponses * 100.0) / totalGraduates : 0.0;
        
        // Crear estadísticas generales
        DashboardOverviewResponse.GeneralStatistics generalStats = DashboardOverviewResponse.GeneralStatistics.builder()
            .totalSurveys(totalSurveys)
            .totalGraduates(totalGraduates)
            .totalResponses(totalResponses)
            .overallResponseRate(overallResponseRate)
            .activeSurveys(activeSurveys)
            .completedSurveys(surveyRepository.findByStatus(SurveyStatus.COMPLETED).stream().count())
            .responsesByEducationCenter(getResponsesByEducationCenter())
            .responsesByGraduationYear(getResponsesByGraduationYear())
            .build();
        
        // Crear gráficos del dashboard
        List<ChartDataResponse> dashboardCharts = generateDashboardCharts();
        
        // Crear KPIs
        List<DashboardOverviewResponse.KpiIndicator> kpiIndicators = generateKpiIndicators(overallResponseRate);
        
        // Obtener encuestas recientes
        List<SurveyResponse> recentSurveys = getRecentSurveys();
        
        return DashboardOverviewResponse.builder()
            .generalStatistics(generalStats)
            .dashboardCharts(dashboardCharts)
            .kpiIndicators(kpiIndicators)
            .recentSurveys(recentSurveys)
            .appliedFilters(DashboardOverviewResponse.DashboardFilters.builder()
                .graduationYear(graduationYear)
                .educationCenterId(educationCenterId)
                .build())
            .build();
    }
    
        @Override
    public List<SurveyStatisticsResponse> compareSurveys(List<Long> surveyIds) {
        return surveyIds.stream()
            .map(this::getSurveyStatistics)
            .collect(Collectors.toList());
    }

    @Override
    public ChartDataResponse getSurveyTrends(Long surveyId, String period) {
        List<GraduateSurveyResponseEntity> responses = graduateSurveyResponseRepository.findBySurveyId(surveyId);
        
        Map<String, Long> trendData = responses.stream()
            .filter(response -> response.getSubmittedAt() != null)
            .collect(Collectors.groupingBy(
                response -> formatPeriod(response.getSubmittedAt(), period),
                Collectors.counting()
            ));
        
        List<String> labels = new ArrayList<>(trendData.keySet());
        Collections.sort(labels);
        List<Object> data = labels.stream()
            .map(trendData::get)
            .collect(Collectors.toList());
        
        return ChartDataResponse.builder()
            .chartType("line")
            .title("Tendencia de Respuestas - " + period.toUpperCase())
            .labels(labels)
            .datasets(Arrays.asList(
                ChartDataResponse.ChartDataset.builder()
                    .label("Respuestas por " + period)
                    .data(data)
                    .borderColor("#2196F3")
                    .backgroundColor("rgba(33, 150, 243, 0.1)")
                    .borderWidth(2)
                    .build()
            ))
            .configuration(createDefaultChartConfiguration("line"))
            .totalResponses((long) responses.size())
            .build();
    }

    @Override
    public ChartDataResponse getDemographicsData(Long surveyId, String demographic) {
        List<GraduateSurveyResponseEntity> responses = graduateSurveyResponseRepository.findBySurveyId(surveyId);
        
        Map<String, Long> demographicData = responses.stream()
            .collect(Collectors.groupingBy(
                response -> getDemographicValue(response, demographic),
                Collectors.counting()
            ));
        
        List<String> labels = new ArrayList<>(demographicData.keySet());
        List<Object> data = labels.stream()
            .map(demographicData::get)
            .collect(Collectors.toList());
        
        return ChartDataResponse.builder()
            .chartType("doughnut")
            .title("Distribución por " + demographic.toUpperCase())
            .labels(labels)
            .datasets(Arrays.asList(
                ChartDataResponse.ChartDataset.builder()
                    .label(demographic)
                    .data(data)
                    .backgroundColors(generateColors(labels.size()))
                    .build()
            ))
            .configuration(createDefaultChartConfiguration("doughnut"))
            .totalResponses((long) responses.size())
            .build();
    }
    
    @Override
    public ResponseEntity<byte[]> exportSurveyData(Long surveyId, String format) {
        String csvData = generateCsvDataFromDb(surveyId);
        String filename = "encuesta_" + surveyId + "_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", filename);
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(csvData.getBytes());
    }
    
    // Métodos auxiliares para obtener datos reales de la base de datos
    
    private List<QuestionStatistics> generateQuestionStatisticsFromDb(Long surveyId, List<QuestionEntity> questions) {
        if (questions == null) return new ArrayList<>();
        
        return questions.stream().map(question -> {
            Long totalResponses = graduateQuestionResponseRepository.countResponsesByQuestionId(question.getId());
            
            QuestionStatistics.QuestionStatisticsBuilder builder = QuestionStatistics.builder()
                .questionId(question.getId())
                .questionText(question.getQuestionText())
                .type(question.getQuestionType())
                .required(question.isRequired())
                .totalResponses(totalResponses)
                .responseRate(totalResponses > 0 ? 100.0 : 0.0);
            
            // Procesar según el tipo de pregunta
            switch (question.getQuestionType()) {
                case SINGLE_CHOICE:
                case MULTIPLE_CHOICE:
                case YES_NO:
                    Map<String, Long> optionCounts = getOptionCountsForQuestion(question.getId());
                    Map<String, Double> percentages = calculatePercentages(optionCounts, totalResponses);
                    builder.optionCounts(optionCounts)
                           .percentages(percentages)
                           .recommendedChartType("bar");
                    break;
                    
                case SCALE:
                    // Para preguntas tipo SCALE, usar las opciones seleccionadas (como "5 - Muy satisfecho")
                    Map<String, Long> scaleOptionCounts = getOptionCountsForQuestion(question.getId());
                    Map<String, Double> scalePercentages = calculatePercentages(scaleOptionCounts, totalResponses);
                    builder.optionCounts(scaleOptionCounts)
                           .percentages(scalePercentages)
                           .recommendedChartType("bar");
                    
                    // Si también quieres estadísticas numéricas, extrae los valores de las opciones
                    if (!scaleOptionCounts.isEmpty()) {
                        try {
                            List<Double> numericValues = scaleOptionCounts.entrySet().stream()
                                .flatMap(entry -> {
                                    // Extraer el número del texto de la opción (ej: "5 - Muy satisfecho" -> 5)
                                    String optionText = entry.getKey();
                                    String[] parts = optionText.split(" - ");
                                    if (parts.length > 0) {
                                        try {
                                            double value = Double.parseDouble(parts[0]);
                                            return Collections.nCopies(entry.getValue().intValue(), value).stream();
                                        } catch (NumberFormatException e) {
                                            return Stream.empty();
                                        }
                                    }
                                    return Stream.empty();
                                })
                                .collect(Collectors.toList());
                            
                            if (!numericValues.isEmpty()) {
                                double average = numericValues.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                                double min = numericValues.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
                                double max = numericValues.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
                                
                                builder.average(average)
                                       .min(min)
                                       .max(max);
                            }
                        } catch (Exception e) {
                            // Si hay error extrayendo valores numéricos, continuar sin estadísticas numéricas
                            System.out.println("No se pudieron extraer valores numéricos de las opciones SCALE para pregunta " + question.getId());
                        }
                    }
                    break;
                    
                case NUMBER:
                    // Para preguntas tipo NUMBER, usar valores numéricos directos
                    List<Integer> numericResponses = graduateQuestionResponseRepository.findNumericResponsesByQuestionId(question.getId());
                    
                    if (!numericResponses.isEmpty()) {
                        // Estadísticas numéricas básicas
                        double average = numericResponses.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
                        double min = numericResponses.stream().mapToDouble(Integer::doubleValue).min().orElse(0.0);
                        double max = numericResponses.stream().mapToDouble(Integer::doubleValue).max().orElse(0.0);
                        
                        // Generar conteos por valor (importante para gráficos de barras)
                        Map<String, Long> numberDistribution = numericResponses.stream()
                            .collect(Collectors.groupingBy(
                                String::valueOf,
                                Collectors.counting()
                            ));
                        
                        Map<String, Double> numberPercentages = calculatePercentages(numberDistribution, totalResponses);
                        
                        builder.average(average)
                               .min(min)
                               .max(max)
                               .optionCounts(numberDistribution)
                               .percentages(numberPercentages)
                               .recommendedChartType("bar");
                    }
                    break;
                    
                case TEXT:
                    List<String> textResponses = graduateQuestionResponseRepository.findTextResponsesByQuestionId(question.getId());
                    if (!textResponses.isEmpty()) {
                        List<String> sampleResponses = textResponses.stream()
                            .limit(5)
                            .collect(Collectors.toList());
                        
                        int avgLength = (int) textResponses.stream()
                            .mapToInt(String::length)
                            .average()
                            .orElse(0.0);
                        
                        builder.sampleResponses(sampleResponses)
                               .averageResponseLength(avgLength)
                               .recommendedChartType("word_cloud");
                    }
                    break;
            }
            
            return builder.build();
        }).collect(Collectors.toList());
    }
    
    private Map<String, Long> getOptionCountsForQuestion(Long questionId) {
        List<Object[]> results = graduateQuestionResponseRepository.countResponsesByOptionForQuestion(questionId);
        Map<String, Long> counts = new HashMap<>();
        
        for (Object[] result : results) {
            String optionText = (String) result[0];
            Long count = (Long) result[1];
            counts.put(optionText, count);
        }
        
        return counts;
    }
    
    private Map<String, Double> calculatePercentages(Map<String, Long> counts, Long total) {
        if (total == 0) return new HashMap<>();
        
        Map<String, Double> percentages = new HashMap<>();
        counts.forEach((key, value) -> {
            double percentage = (value * 100.0) / total;
            percentages.put(key, Math.round(percentage * 100.0) / 100.0);
        });
        
        return percentages;
    }
    
    private Map<String, Long> getResponsesByLocation(Long surveyId) {
        // Implementar consulta para obtener respuestas por ubicación
        // Esto dependería de cómo tienes modelada la ubicación en GraduateEntity
        return new HashMap<>();
    }
    
    private Map<String, Long> getResponsesByIndustry(Long surveyId) {
        // Implementar consulta para obtener respuestas por industria
        // Esto dependería de cómo tienes modelada la industria
        return new HashMap<>();
    }
    
    private Map<String, Long> getResponsesByGender(Long surveyId) {
        // Obtener respuestas agrupadas por género
        Map<String, Long> genderCounts = new HashMap<>();
        
        List<GraduateSurveyResponseEntity> responses = graduateSurveyResponseRepository.findBySurveyId(surveyId);
        
        Map<String, Long> counts = responses.stream()
            .collect(Collectors.groupingBy(
                response -> response.getGraduate().getUser().getGenero() != null ? 
                    response.getGraduate().getUser().getGenero() : "No especificado",
                Collectors.counting()
            ));
        
        return counts;
    }
    
    private Map<String, Long> getResponsesByEmploymentStatus(Long surveyId) {
        // Implementar consulta para obtener respuestas por estado laboral
        // Esto dependería de cómo tienes modelado el estado laboral
        return new HashMap<>();
    }
    
    private Map<String, Long> getResponsesByMonth(Long surveyId) {
        List<GraduateSurveyResponseEntity> responses = graduateSurveyResponseRepository.findBySurveyId(surveyId);
        
        Map<String, Long> monthCounts = responses.stream()
            .filter(response -> response.getSubmittedAt() != null)
            .collect(Collectors.groupingBy(
                response -> response.getSubmittedAt().getMonth().toString(),
                Collectors.counting()
            ));
        
        return monthCounts;
    }
    
    private String generateCsvDataFromDb(Long surveyId) {
        StringBuilder csv = new StringBuilder();
        csv.append("Pregunta,Tipo,Respuesta,Graduado,Fecha\n");
        
        List<GraduateQuestionResponseEntity> responses = graduateQuestionResponseRepository.findBySurveyId(surveyId);
        
        for (GraduateQuestionResponseEntity response : responses) {
            String questionText = response.getQuestion().getQuestionText().replace(",", ";");
            String questionType = response.getQuestion().getQuestionType().toString();
            String answerText = "";
            
            if (response.getTextResponse() != null) {
                answerText = response.getTextResponse().replace(",", ";");
            } else if (response.getNumericResponse() != null) {
                answerText = response.getNumericResponse().toString();
            } else if (response.getSelectedOptions() != null && !response.getSelectedOptions().isEmpty()) {
                answerText = response.getSelectedOptions().stream()
                    .map(option -> option.getOptionText())
                    .collect(Collectors.joining("; "));
            }
            
            String graduateName = response.getGraduateSurveyResponse().getGraduate().getUser().getNombres() + " " +
                                response.getGraduateSurveyResponse().getGraduate().getUser().getApellidos();
            String submittedDate = response.getGraduateSurveyResponse().getSubmittedAt() != null ?
                                 response.getGraduateSurveyResponse().getSubmittedAt().toString() : "";
            
            csv.append(String.format("%s,%s,%s,%s,%s\n", 
                questionText, questionType, answerText, graduateName, submittedDate));
        }
        
        return csv.toString();
    }
    
    private Map<String, Long> getWordFrequency(List<String> textResponses) {
        Map<String, Long> wordCount = new HashMap<>();
        
        for (String response : textResponses) {
            if (response != null && !response.trim().isEmpty()) {
                String[] words = response.toLowerCase()
                    .replaceAll("[^a-záéíóúñü\\s]", "")
                    .split("\\s+");
                
                for (String word : words) {
                    if (word.length() > 3) { // Solo palabras de más de 3 caracteres
                        wordCount.merge(word, 1L, Long::sum);
                    }
                }
            }
        }
        
        return wordCount;
    }
    
    private List<String> generateColors(int count) {
        String[] colors = {"#2196F3", "#4CAF50", "#FF9800", "#F44336", "#9C27B0", "#00BCD4", "#FFEB3B", "#795548"};
        List<String> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(colors[i % colors.length]);
        }
        return result;
    }
    
    private ChartDataResponse.ChartConfiguration createDefaultChartConfiguration(String chartType) {
        return ChartDataResponse.ChartConfiguration.builder()
            .responsive(true)
            .maintainAspectRatio(false)
            .legend(ChartDataResponse.ChartConfiguration.ChartLegend.builder()
                .display(true)
                .position("top")
                .build())
            .tooltip(ChartDataResponse.ChartConfiguration.ChartTooltip.builder()
                .enabled(true)
                .mode("point")
                .build())
            .build();
    }
    
    private Map<String, Long> getResponsesByEducationCenter() {
        List<GraduateSurveyResponseEntity> responses = graduateSurveyResponseRepository.findAll();
        
        return responses.stream()
            .collect(Collectors.groupingBy(
                response -> {
                    // Aquí necesitarías acceder al centro educativo del graduado
                    // Dependiendo de tu modelo de datos
                    return "Centro Educativo"; // Placeholder
                },
                Collectors.counting()
            ));
    }
    
    private Map<String, Long> getResponsesByGraduationYear() {
        List<GraduateSurveyResponseEntity> responses = graduateSurveyResponseRepository.findAll();
        
        return responses.stream()
            .collect(Collectors.groupingBy(
                response -> {
                    // Aquí necesitarías acceder al año de graduación
                    // Dependiendo de tu modelo de datos
                    return "2024"; // Placeholder
                },
                Collectors.counting()
            ));
    }
    
    private List<ChartDataResponse> generateDashboardCharts() {
        List<ChartDataResponse> charts = new ArrayList<>();
        
        // Gráfico de encuestas por estado
        Map<String, Long> surveysByStatus = Arrays.stream(SurveyStatus.values())
            .collect(Collectors.toMap(
                Enum::name,
                status -> surveyRepository.findByStatus(status).stream().count()
            ));
        
        charts.add(ChartDataResponse.builder()
            .chartType("pie")
            .title("Encuestas por Estado")
            .labels(new ArrayList<>(surveysByStatus.keySet()))
            .datasets(Arrays.asList(
                ChartDataResponse.ChartDataset.builder()
                    .label("Cantidad")
                    .data(surveysByStatus.values().stream().collect(Collectors.toList()))
                    .backgroundColors(generateColors(surveysByStatus.size()))
                    .build()
            ))
            .configuration(createDefaultChartConfiguration("pie"))
            .build());
        
        // Gráfico de respuestas por mes
        Map<String, Long> responsesByMonth = graduateSurveyResponseRepository.findAll().stream()
            .filter(response -> response.getSubmittedAt() != null)
            .collect(Collectors.groupingBy(
                response -> response.getSubmittedAt().getMonth().toString(),
                Collectors.counting()
            ));
        
        charts.add(ChartDataResponse.builder()
            .chartType("line")
            .title("Respuestas por Mes")
            .labels(new ArrayList<>(responsesByMonth.keySet()))
            .datasets(Arrays.asList(
                ChartDataResponse.ChartDataset.builder()
                    .label("Respuestas")
                    .data(responsesByMonth.values().stream().collect(Collectors.toList()))
                    .borderColor("#2196F3")
                    .backgroundColor("rgba(33, 150, 243, 0.1)")
                    .build()
            ))
            .configuration(createDefaultChartConfiguration("line"))
            .build());
        
        return charts;
    }
    
    private List<DashboardOverviewResponse.KpiIndicator> generateKpiIndicators(Double responseRate) {
        List<DashboardOverviewResponse.KpiIndicator> kpis = new ArrayList<>();
        
        kpis.add(DashboardOverviewResponse.KpiIndicator.builder()
            .name("Tasa de Respuesta Global")
            .value(String.format("%.1f%%", responseRate))
            .unit("%")
            .trend(responseRate > 70 ? "up" : responseRate > 50 ? "stable" : "down")
            .changePercentage(0.0) // Aquí podrías calcular el cambio vs período anterior
            .description("Porcentaje de graduados que han respondido encuestas")
            .color(responseRate > 70 ? "#4CAF50" : responseRate > 50 ? "#FF9800" : "#F44336")
            .build());
        
        Long activeSurveys = surveyRepository.findByStatus(SurveyStatus.ACTIVE).stream().count();
        kpis.add(DashboardOverviewResponse.KpiIndicator.builder()
            .name("Encuestas Activas")
            .value(activeSurveys.toString())
            .unit("encuestas")
            .trend("stable")
            .changePercentage(0.0)
            .description("Número de encuestas actualmente abiertas")
            .color("#2196F3")
            .build());
        
        Long totalGraduates = graduateRepository.countByUserEstado("1");
        kpis.add(DashboardOverviewResponse.KpiIndicator.builder()
            .name("Total Graduados")
            .value(totalGraduates.toString())
            .unit("graduados")
            .trend("up")
            .changePercentage(0.0)
            .description("Número total de graduados activos")
            .color("#9C27B0")
            .build());
        
        return kpis;
    }
    
    private List<SurveyResponse> getRecentSurveys() {
        List<SurveyEntity> recentSurveys = surveyRepository.findAll().stream()
            .sorted((s1, s2) -> s2.getCreatedDate().compareTo(s1.getCreatedDate()))
            .limit(5)
            .toList();
        
        return recentSurveys.stream()
            .map(survey -> surveyRepositoryPort.getDomain(survey.getId()))
            .collect(Collectors.toList());
    }
     
     private String formatPeriod(LocalDateTime dateTime, String period) {
         switch (period.toLowerCase()) {
             case "daily":
                 return dateTime.toLocalDate().toString();
             case "weekly":
                 int weekOfYear = dateTime.getDayOfYear() / 7 + 1;
                 return "Semana " + weekOfYear + "-" + dateTime.getYear();
             case "monthly":
                 return dateTime.getMonth().toString() + " " + dateTime.getYear();
             default:
                 return dateTime.toLocalDate().toString();
         }
     }
     
     private String getDemographicValue(GraduateSurveyResponseEntity response, String demographic) {
         switch (demographic.toLowerCase()) {
             case "gender":
             case "genero":
                 return response.getGraduate().getUser().getGenero() != null ? 
                     response.getGraduate().getUser().getGenero() : "No especificado";
             case "location":
             case "ubicacion":
                 // Aquí deberías acceder al campo de ubicación en tu modelo
                 return "Ubicación por definir"; // Placeholder
             case "industry":
             case "industria":
                 // Aquí deberías acceder al campo de industria en tu modelo
                 return "Industria por definir"; // Placeholder
             case "employment":
             case "empleo":
                 // Aquí deberías acceder al estado laboral en tu modelo
                 return "Estado laboral por definir"; // Placeholder
             default:
                 return "Sin categoría";
         }
     }
} 