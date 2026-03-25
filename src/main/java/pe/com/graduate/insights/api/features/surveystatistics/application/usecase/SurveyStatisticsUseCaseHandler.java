package pe.com.graduate.insights.api.features.surveystatistics.application.usecase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.surveystatistics.application.dto.ChartDataResponse;
import pe.com.graduate.insights.api.features.surveystatistics.application.dto.DashboardOverviewResponse;
import pe.com.graduate.insights.api.features.surveystatistics.application.dto.QuestionStatistics;
import pe.com.graduate.insights.api.features.surveystatistics.application.dto.SurveyStatisticsResponse;
import pe.com.graduate.insights.api.features.surveystatistics.application.ports.input.SurveyStatisticsUseCase;
import pe.com.graduate.insights.api.features.surveystatistics.domain.port.output.SurveyStatisticsRepositoryPort;
import pe.com.graduate.insights.api.features.survey.application.ports.output.SurveyRepositoryPort;
import pe.com.graduate.insights.api.features.survey.application.dto.QuestionResponse;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyResponse;
import pe.com.graduate.insights.api.features.survey.domain.model.SurveyStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyStatisticsUseCaseHandler implements SurveyStatisticsUseCase {

  private static final String LABEL_RESPONSES = "Respuestas";
  private static final String COLOR_BLUE = "#2196F3";
  private static final String COLOR_GREEN = "#4CAF50";
  private static final String COLOR_ORANGE = "#FF9800";
  private static final String COLOR_RED = "#F44336";

  private static final Pattern SCALE_NUMERIC_PATTERN = Pattern.compile("^(\\d+(?:\\.\\d+)?)");

  private final SurveyRepositoryPort surveyRepositoryPort;
  private final SurveyStatisticsRepositoryPort surveyStatisticsRepositoryPort;

  @Override
  public SurveyStatisticsResponse getSurveyStatistics(Long surveyId) {
    SurveyResponse survey = surveyRepositoryPort.getDomain(surveyId);

    List<SurveyStatisticsRepositoryPort.SurveyResponseData> surveyResponses =
      surveyStatisticsRepositoryPort.findSurveyResponsesBySurveyId(surveyId);
    List<SurveyStatisticsRepositoryPort.SurveyResponseData> completedResponses =
      surveyStatisticsRepositoryPort.findCompletedSurveyResponsesBySurveyId(surveyId);

    Long totalGraduates = surveyStatisticsRepositoryPort.countActiveGraduates();

    // Calcular metricas
    int totalResponses = surveyResponses.size();
    int completedCount = completedResponses.size();
    double responseRate = totalGraduates > 0 ? (totalResponses * 100.0) / totalGraduates : 0.0;
    double completionRate = totalResponses > 0 ? (completedCount * 100.0) / totalResponses : 0.0;

    // Obtener estadisticas por pregunta
    List<QuestionStatistics> questionStats = generateQuestionStatisticsFromDb(survey.getQuestions());

    // Obtener datos demograficos
    Map<String, Long> responsesByLocation = getResponsesByLocation(surveyId);
    Map<String, Long> responsesByIndustry = getResponsesByIndustry();
    Map<String, Long> responsesByGender = getResponsesByGender(surveyId);
    Map<String, Long> responsesByEmploymentStatus = getResponsesByEmploymentStatus(surveyId);

    // Obtener datos temporales
    Map<String, Long> responsesByMonth = getResponsesByMonth(surveyId);

    return SurveyStatisticsResponse.builder()
        .surveyId(surveyId)
        .surveyTitle(survey.getTitle())
        .surveyDescription(survey.getDescription())
        .surveyType(survey.getSurveyType())
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
    List<SurveyStatisticsRepositoryPort.QuestionResponseData> responses =
        surveyStatisticsRepositoryPort.findQuestionResponsesBySurveyIdAndQuestionId(surveyId, questionId);

    if (responses.isEmpty()) {
      return ChartDataResponse.builder()
          .chartType(chartType)
          .title("Sin datos disponibles")
          .labels(List.of("Sin respuestas"))
          .totalResponses(0L)
          .build();
    }

        QuestionResponse question =
          surveyRepositoryPort.getDomain(surveyId).getQuestions().stream()
            .filter(item -> item.getId().equals(questionId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada"));
        Long totalResponses = surveyStatisticsRepositoryPort.countQuestionResponsesByQuestionId(questionId);

    ChartDataResponse.ChartDataResponseBuilder builder =
        ChartDataResponse.builder()
            .chartType(chartType)
          .title(question.getQuestionText())
            .totalResponses(totalResponses);

    switch (question.getQuestionType()) {
      case SINGLE_CHOICE:
      case MULTIPLE_CHOICE:
      case YES_NO:
        Map<String, Long> optionCounts = getOptionCountsForQuestion(questionId);
        List<String> labels = new ArrayList<>(optionCounts.keySet());
        List<Object> data = labels.stream().map(optionCounts::get).collect(Collectors.toList());

        builder
            .labels(labels)
            .datasets(
                Arrays.asList(
                    ChartDataResponse.ChartDataset.builder()
                      .label(LABEL_RESPONSES)
                        .data(data)
                        .backgroundColors(generateColors(labels.size()))
                        .build()));
        break;

      case SCALE:
        // Para preguntas SCALE, usar las opciones seleccionadas
        Map<String, Long> scaleOptionCounts = getOptionCountsForQuestion(questionId);
        List<String> scaleLabels = new ArrayList<>(scaleOptionCounts.keySet());
        List<Object> scaleData =
            scaleLabels.stream().map(scaleOptionCounts::get).collect(Collectors.toList());

        builder
            .labels(scaleLabels)
            .datasets(
                Arrays.asList(
                    ChartDataResponse.ChartDataset.builder()
                      .label(LABEL_RESPONSES)
                        .data(scaleData)
                        .backgroundColors(generateColors(scaleLabels.size()))
                        .build()));
        break;

      case NUMBER:
        // Para preguntas NUMBER, usar valores numericos directos
        List<Integer> numericResponses =
            surveyStatisticsRepositoryPort.findNumericResponsesByQuestionId(questionId);
        Map<String, Long> distribution =
            numericResponses.stream()
                .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()));

        List<String> numLabels = new ArrayList<>(distribution.keySet());
        Collections.sort(numLabels);
        List<Object> numData =
            numLabels.stream().map(distribution::get).collect(Collectors.toList());

        builder
            .labels(numLabels)
            .datasets(
                Arrays.asList(
                    ChartDataResponse.ChartDataset.builder()
                        .label("Distribucion")
                        .data(numData)
                      .backgroundColor(COLOR_BLUE)
                        .build()));
        break;

      case TEXT:
        // Para texto, mostrar palabras mas comunes
        List<String> textResponses =
            surveyStatisticsRepositoryPort.findTextResponsesByQuestionId(questionId);
        Map<String, Long> wordCount = getWordFrequency(textResponses);

        List<String> topWords =
            wordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Object> wordData = topWords.stream().map(wordCount::get).collect(Collectors.toList());

        builder
            .labels(topWords)
            .datasets(
                Arrays.asList(
                    ChartDataResponse.ChartDataset.builder()
                        .label("Frecuencia de palabras")
                        .data(wordData)
                      .backgroundColor(COLOR_GREEN)
                        .build()));
        break;

      default:
        throw new IllegalArgumentException(
            "Tipo de pregunta no soportado: " + question.getQuestionType());
    }

    builder.configuration(createDefaultChartConfiguration());

    return builder.build();
  }

  @Override
  public DashboardOverviewResponse getDashboardOverview(Integer graduationYear) {
    Long totalSurveys = surveyStatisticsRepositoryPort.countSurveys();
    Long activeSurveys = surveyStatisticsRepositoryPort.countSurveysByStatus(SurveyStatus.ACTIVE);

    // Filtrar graduados por anio de graduacion si se especifica
    Long totalGraduates;
    Long totalResponses;

    if (graduationYear != null) {
      totalGraduates =
        surveyStatisticsRepositoryPort.findAllSurveyResponses().stream()
          .filter(response -> response.graduationYear() != null)
          .map(SurveyStatisticsRepositoryPort.SurveyResponseData::graduationYear)
          .distinct()
          .filter(year -> year.equals(graduationYear))
              .count();

      totalResponses =
        surveyStatisticsRepositoryPort.findAllSurveyResponses().stream()
          .filter(response -> response.graduationYear() != null)
          .filter(response -> response.graduationYear().equals(graduationYear))
              .count();
    } else {
      totalGraduates = surveyStatisticsRepositoryPort.countActiveGraduates();
      totalResponses = (long) surveyStatisticsRepositoryPort.findAllSurveyResponses().size();
    }

    Double overallResponseRate =
        totalGraduates > 0 ? (totalResponses * 100.0) / totalGraduates : 0.0;

    // Crear estadisticas generales
    DashboardOverviewResponse.GeneralStatistics generalStats =
        DashboardOverviewResponse.GeneralStatistics.builder()
            .totalSurveys(totalSurveys)
            .totalGraduates(totalGraduates)
            .totalResponses(totalResponses)
            .overallResponseRate(overallResponseRate)
            .activeSurveys(activeSurveys)
            .completedSurveys(surveyStatisticsRepositoryPort.countSurveysByStatus(SurveyStatus.COMPLETED))
            .responsesByGraduationYear(getResponsesByGraduationYear(graduationYear))
            .build();

    // Crear graficos del dashboard
    List<ChartDataResponse> dashboardCharts = generateDashboardCharts(graduationYear);

    // Crear KPIs
    List<DashboardOverviewResponse.KpiIndicator> kpiIndicators =
        generateKpiIndicators(overallResponseRate, totalGraduates);

    // Obtener encuestas recientes
    List<SurveyResponse> recentSurveys = getRecentSurveys();

    return DashboardOverviewResponse.builder()
        .generalStatistics(generalStats)
        .dashboardCharts(dashboardCharts)
        .kpiIndicators(kpiIndicators)
        .recentSurveys(recentSurveys)
        .appliedFilters(
            DashboardOverviewResponse.DashboardFilters.builder()
                .graduationYear(graduationYear)
                .build())
        .build();
  }

  @Override
  public List<SurveyStatisticsResponse> compareSurveys(List<Long> surveyIds) {
    return surveyIds.stream().map(this::getSurveyStatistics).collect(Collectors.toList());
  }

  @Override
  public ChartDataResponse getSurveyTrends(Long surveyId, String period) {
    List<SurveyStatisticsRepositoryPort.SurveyResponseData> responses =
      surveyStatisticsRepositoryPort.findSurveyResponsesBySurveyId(surveyId);

    Map<String, Long> trendData =
        responses.stream()
        .filter(response -> response.submittedAt() != null)
            .collect(
                Collectors.groupingBy(
                response -> formatPeriod(response.submittedAt(), period),
                    Collectors.counting()));

    List<String> labels = new ArrayList<>(trendData.keySet());
    Collections.sort(labels);
    List<Object> data = labels.stream().map(trendData::get).collect(Collectors.toList());

    return ChartDataResponse.builder()
        .chartType("line")
        .title("Tendencia de Respuestas - " + period.toUpperCase())
        .labels(labels)
        .datasets(
            Arrays.asList(
                ChartDataResponse.ChartDataset.builder()
                    .label("Respuestas por " + period)
                    .data(data)
                    .borderColor(COLOR_BLUE)
                    .backgroundColor("rgba(33, 150, 243, 0.1)")
                    .borderWidth(2)
                    .build()))
        .configuration(createDefaultChartConfiguration())
        .totalResponses((long) responses.size())
        .build();
  }

  @Override
  public ChartDataResponse getDemographicsData(Long surveyId, String demographic) {
    List<SurveyStatisticsRepositoryPort.SurveyResponseData> responses =
      surveyStatisticsRepositoryPort.findSurveyResponsesBySurveyId(surveyId);

    Map<String, Long> demographicData =
        responses.stream()
            .collect(
                Collectors.groupingBy(
                    response -> getDemographicValue(response, demographic), Collectors.counting()));

    List<String> labels = new ArrayList<>(demographicData.keySet());
    List<Object> data = labels.stream().map(demographicData::get).collect(Collectors.toList());

    return ChartDataResponse.builder()
        .chartType("doughnut")
        .title("Distribucion por " + demographic.toUpperCase())
        .labels(labels)
        .datasets(
            Arrays.asList(
                ChartDataResponse.ChartDataset.builder()
                    .label(demographic)
                    .data(data)
                    .backgroundColors(generateColors(labels.size()))
                    .build()))
        .configuration(createDefaultChartConfiguration())
        .totalResponses((long) responses.size())
        .build();
  }

  @Override
  public ResponseEntity<byte[]> exportSurveyData(Long surveyId, String format) {
    String csvData = generateCsvDataFromDb(surveyId);
    String filename =
        "encuesta_"
            + surveyId
            + "_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            + ".csv";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("text/csv"));
    headers.setContentDispositionFormData("attachment", filename);

    return ResponseEntity.ok().headers(headers).body(csvData.getBytes());
  }

  // Metodos auxiliares para obtener datos reales de la base de datos

  private List<QuestionStatistics> generateQuestionStatisticsFromDb(List<QuestionResponse> questions) {
    if (questions == null) {
      return new ArrayList<>();
    }

    return questions.stream().map(this::buildQuestionStatistics).collect(Collectors.toList());
  }

  private QuestionStatistics buildQuestionStatistics(QuestionResponse question) {
    Long totalResponses =
        surveyStatisticsRepositoryPort.countQuestionResponsesByQuestionId(question.getId());

    QuestionStatistics.QuestionStatisticsBuilder builder =
        QuestionStatistics.builder()
            .questionId(question.getId())
            .questionText(question.getQuestionText())
            .type(question.getQuestionType())
            .required(question.isRequired())
            .totalResponses(totalResponses)
            .responseRate(totalResponses > 0 ? 100.0 : 0.0);

    switch (question.getQuestionType()) {
      case SINGLE_CHOICE:
      case MULTIPLE_CHOICE:
      case YES_NO:
        applyChoiceQuestionStatistics(builder, question.getId(), totalResponses);
        break;
      case SCALE:
        applyScaleQuestionStatistics(builder, question.getId(), totalResponses);
        break;
      case NUMBER:
        applyNumberQuestionStatistics(builder, question.getId(), totalResponses);
        break;
      case TEXT:
        applyTextQuestionStatistics(builder, question.getId());
        break;
      default:
        throw new IllegalArgumentException(
            "Tipo de pregunta no soportado: " + question.getQuestionType());
    }

    return builder.build();
  }

  private void applyChoiceQuestionStatistics(
      QuestionStatistics.QuestionStatisticsBuilder builder, Long questionId, Long totalResponses) {
    Map<String, Long> optionCounts = getOptionCountsForQuestion(questionId);
    Map<String, Double> percentages = calculatePercentages(optionCounts, totalResponses);
    builder.optionCounts(optionCounts).percentages(percentages).recommendedChartType("bar");
  }

  private void applyScaleQuestionStatistics(
      QuestionStatistics.QuestionStatisticsBuilder builder, Long questionId, Long totalResponses) {
    Map<String, Long> scaleOptionCounts = getOptionCountsForQuestion(questionId);
    Map<String, Double> scalePercentages = calculatePercentages(scaleOptionCounts, totalResponses);
    builder
        .optionCounts(scaleOptionCounts)
        .percentages(scalePercentages)
        .recommendedChartType("bar");

    if (scaleOptionCounts.isEmpty()) {
      return;
    }

    try {
      List<Double> numericValues = extractScaleNumericValues(scaleOptionCounts);
      if (numericValues.isEmpty()) {
        return;
      }

      double average = numericValues.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
      double min = numericValues.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
      double max = numericValues.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

      // Compute median
      Collections.sort(numericValues);
      int n = numericValues.size();
      double median = n % 2 == 0
          ? (numericValues.get(n / 2 - 1) + numericValues.get(n / 2)) / 2.0
          : numericValues.get(n / 2);

      // Compute mode
      Map<Double, Long> frequencyMap = numericValues.stream()
          .collect(Collectors.groupingBy(v -> v, Collectors.counting()));
      double mode = frequencyMap.entrySet().stream()
          .max(Map.Entry.comparingByValue())
          .map(Map.Entry::getKey).orElse(0.0);

      // Compute standard deviation
      double variance = numericValues.stream()
          .mapToDouble(v -> Math.pow(v - average, 2)).average().orElse(0.0);
      double stddev = Math.sqrt(variance);

      builder.average(average).min(min).max(max)
          .median(median).mode(mode).standardDeviation(stddev);
    } catch (Exception e) {
      log.debug(
          "No se pudieron extraer valores numericos de las opciones SCALE para pregunta {}",
          questionId,
          e);
    }
  }

  private List<Double> extractScaleNumericValues(Map<String, Long> scaleOptionCounts) {
    return scaleOptionCounts.entrySet().stream()
        .flatMap(
            entry -> {
              Matcher matcher = SCALE_NUMERIC_PATTERN.matcher(entry.getKey().trim());
              if (matcher.find()) {
                try {
                  double value = Double.parseDouble(matcher.group(1));
                  return Collections.nCopies(entry.getValue().intValue(), value).stream();
                } catch (NumberFormatException e) {
                  return Stream.empty();
                }
              }
              return Stream.empty();
            })
        .collect(Collectors.toList());
  }

  private void applyNumberQuestionStatistics(
      QuestionStatistics.QuestionStatisticsBuilder builder, Long questionId, Long totalResponses) {
    List<Integer> numericResponses =
        surveyStatisticsRepositoryPort.findNumericResponsesByQuestionId(questionId);
    if (numericResponses.isEmpty()) {
      return;
    }

    double average = numericResponses.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
    double min = numericResponses.stream().mapToDouble(Integer::doubleValue).min().orElse(0.0);
    double max = numericResponses.stream().mapToDouble(Integer::doubleValue).max().orElse(0.0);

    // Compute median
    List<Double> sortedValues = numericResponses.stream()
        .map(Integer::doubleValue)
        .sorted()
        .collect(Collectors.toList());
    int n = sortedValues.size();
    double median = n % 2 == 0
        ? (sortedValues.get(n / 2 - 1) + sortedValues.get(n / 2)) / 2.0
        : sortedValues.get(n / 2);

    // Compute mode
    Map<Double, Long> frequencyMap = sortedValues.stream()
        .collect(Collectors.groupingBy(v -> v, Collectors.counting()));
    double mode = frequencyMap.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey).orElse(0.0);

    // Compute standard deviation
    double variance = sortedValues.stream()
        .mapToDouble(v -> Math.pow(v - average, 2)).average().orElse(0.0);
    double stddev = Math.sqrt(variance);

    Map<String, Long> numberDistribution =
        numericResponses.stream().collect(Collectors.groupingBy(String::valueOf, Collectors.counting()));
    Map<String, Double> numberPercentages = calculatePercentages(numberDistribution, totalResponses);

    builder
        .average(average)
        .min(min)
        .max(max)
        .median(median)
        .mode(mode)
        .standardDeviation(stddev)
        .optionCounts(numberDistribution)
        .percentages(numberPercentages)
        .recommendedChartType("bar");
  }

  private void applyTextQuestionStatistics(
      QuestionStatistics.QuestionStatisticsBuilder builder, Long questionId) {
    List<String> textResponses =
        surveyStatisticsRepositoryPort.findTextResponsesByQuestionId(questionId);
    if (textResponses.isEmpty()) {
      return;
    }

    List<String> sampleResponses = textResponses.stream().limit(5).collect(Collectors.toList());
    int avgLength = (int) textResponses.stream().mapToInt(String::length).average().orElse(0.0);

    Map<String, Long> wordFrequency = getWordFrequency(textResponses);
    Map<String, Long> topWords =
        wordFrequency.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new));

    long totalWords = wordFrequency.values().stream().mapToLong(Long::longValue).sum();
    Map<String, Double> wordPercentages = new HashMap<>();
    topWords.forEach(
        (word, count) -> {
          double percentage = totalWords > 0 ? (count * 100.0) / totalWords : 0.0;
          wordPercentages.put(word, Math.round(percentage * 100.0) / 100.0);
        });

    int minLength = textResponses.stream().mapToInt(String::length).min().orElse(0);
    int maxLength = textResponses.stream().mapToInt(String::length).max().orElse(0);

    builder
        .sampleResponses(sampleResponses)
        .averageResponseLength(avgLength)
        .optionCounts(topWords)
        .percentages(wordPercentages)
        .min((double) minLength)
        .max((double) maxLength)
        .recommendedChartType("word_cloud");
  }

  private Map<String, Long> getOptionCountsForQuestion(Long questionId) {
    return surveyStatisticsRepositoryPort.countResponsesByOptionForQuestion(questionId);
  }

  private Map<String, Double> calculatePercentages(Map<String, Long> counts, Long total) {
    if (total == 0) {
      return new HashMap<>();
    }

    Map<String, Double> percentages = new HashMap<>();
    counts.forEach(
        (key, value) -> {
          double percentage = (value * 100.0) / total;
          percentages.put(key, Math.round(percentage * 100.0) / 100.0);
        });

    return percentages;
  }

  private Map<String, Long> getResponsesByLocation(Long surveyId) {
    List<SurveyStatisticsRepositoryPort.SurveyResponseData> responses =
        surveyStatisticsRepositoryPort.findSurveyResponsesBySurveyId(surveyId);

    return responses.stream()
        .collect(
            Collectors.groupingBy(
                response ->
                    response.departamento() != null && !response.departamento().isBlank()
                        ? response.departamento()
                        : "No especificado",
                Collectors.counting()));
  }

  private Map<String, Long> getResponsesByIndustry() {
    // Implementar consulta para obtener respuestas por industria
    // Esto dependeria de como tienes modelada la industria
    return new HashMap<>();
  }

  private Map<String, Long> getResponsesByGender(Long surveyId) {
    List<SurveyStatisticsRepositoryPort.SurveyResponseData> responses =
      surveyStatisticsRepositoryPort.findSurveyResponsesBySurveyId(surveyId);

    return responses.stream()
      .collect(
        Collectors.groupingBy(
          response ->
            response.gender() != null
              ? response.gender()
              : "No especificado",
          Collectors.counting()));
  }

  private Map<String, Long> getResponsesByEmploymentStatus(Long surveyId) {
    List<SurveyStatisticsRepositoryPort.SurveyResponseData> responses =
        surveyStatisticsRepositoryPort.findSurveyResponsesBySurveyId(surveyId);

    return responses.stream()
        .collect(
            Collectors.groupingBy(
                response -> {
                  if (response.hasCurrentJob() != null && response.hasCurrentJob()) {
                    return "Empleado";
                  } else if (response.hasCurrentJob() != null) {
                    return "Desempleado";
                  }
                  return "Sin informacion";
                },
                Collectors.counting()));
  }

  private Map<String, Long> getResponsesByMonth(Long surveyId) {
    List<SurveyStatisticsRepositoryPort.SurveyResponseData> responses =
      surveyStatisticsRepositoryPort.findSurveyResponsesBySurveyId(surveyId);

    return responses.stream()
      .filter(response -> response.submittedAt() != null)
      .collect(
        Collectors.groupingBy(
          response -> response.submittedAt().getMonth().toString(), Collectors.counting()));
  }

  private String generateCsvDataFromDb(Long surveyId) {
    StringBuilder csv = new StringBuilder();
    csv.append("Pregunta,Tipo,Respuesta,Graduado,Fecha\n");

    List<SurveyStatisticsRepositoryPort.QuestionResponseData> responses =
        surveyStatisticsRepositoryPort.findQuestionResponsesBySurveyId(surveyId);

    for (SurveyStatisticsRepositoryPort.QuestionResponseData response : responses) {
      String questionText = response.questionText().replace(",", ";");
      String questionType = response.questionType().toString();
      String answerText = "";

      if (response.textResponse() != null) {
        answerText = response.textResponse().replace(",", ";");
      } else if (response.numericResponse() != null) {
        answerText = response.numericResponse().toString();
      } else if (response.selectedOptionTexts() != null
          && !response.selectedOptionTexts().isEmpty()) {
        answerText = response.selectedOptionTexts().stream().collect(Collectors.joining("; "));
      }

      String graduateName = response.graduateFullName();
      String submittedDate =
          response.submittedAt() != null
              ? response.submittedAt().toString()
              : "";

      csv.append(
          String.format(
            "%s,%s,%s,%s,%s%n",
              questionText, questionType, answerText, graduateName, submittedDate));
    }

    return csv.toString();
  }

  private Map<String, Long> getWordFrequency(List<String> textResponses) {
    Map<String, Long> wordCount = new HashMap<>();

    for (String response : textResponses) {
      if (response != null && !response.trim().isEmpty()) {
        String[] words = response.toLowerCase().replaceAll("[^a-z\u00e1\u00e9\u00ed\u00f3\u00fa\u00f1\u00fc\\s]", "").split("\\s+");

        for (String word : words) {
          if (word.length() > 3) { // Solo palabras de mas de 3 caracteres
            wordCount.merge(word, 1L, Long::sum);
          }
        }
      }
    }

    return wordCount;
  }

  private List<String> generateColors(int count) {
    String[] colors = {
      COLOR_BLUE, COLOR_GREEN, COLOR_ORANGE, COLOR_RED, "#9C27B0", "#00BCD4", "#FFEB3B", "#795548"
    };
    List<String> result = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      result.add(colors[i % colors.length]);
    }
    return result;
  }

  private ChartDataResponse.ChartConfiguration createDefaultChartConfiguration() {
    return ChartDataResponse.ChartConfiguration.builder()
        .responsive(true)
        .maintainAspectRatio(false)
        .legend(
            ChartDataResponse.ChartConfiguration.ChartLegend.builder()
                .display(true)
                .position("top")
                .build())
        .tooltip(
            ChartDataResponse.ChartConfiguration.ChartTooltip.builder()
                .enabled(true)
                .mode("point")
                .build())
        .build();
  }

  private Map<String, Long> getResponsesByGraduationYear(Integer graduationYear) {
    List<SurveyStatisticsRepositoryPort.SurveyResponseData> responses =
        surveyStatisticsRepositoryPort.findAllSurveyResponses();

    return responses.stream()
        .filter(
            response ->
                graduationYear == null
                    || (response.graduationYear() != null
                        && response.graduationYear() == graduationYear))
        .collect(
            Collectors.groupingBy(
                response -> {
                  if (response.graduationYear() != null) {
                    return String.valueOf(response.graduationYear());
                  }
                  return "Sin fecha de graduacion";
                },
                Collectors.counting()));
  }

  private List<ChartDataResponse> generateDashboardCharts(Integer graduationYear) {
    List<ChartDataResponse> charts = new ArrayList<>();

    // Grafico de encuestas por estado (no cambia con el filtro de anio)
    Map<String, Long> surveysByStatus =
        Arrays.stream(SurveyStatus.values())
            .collect(
                Collectors.toMap(
            Enum::name, surveyStatisticsRepositoryPort::countSurveysByStatus));

    charts.add(
        ChartDataResponse.builder()
            .chartType("pie")
            .title("Encuestas por Estado")
            .labels(new ArrayList<>(surveysByStatus.keySet()))
            .datasets(
                Arrays.asList(
                    ChartDataResponse.ChartDataset.builder()
                        .label("Cantidad")
                        .data(surveysByStatus.values().stream().collect(Collectors.toList()))
                        .backgroundColors(generateColors(surveysByStatus.size()))
                        .build()))
            .configuration(createDefaultChartConfiguration())
            .build());

    // Grafico de respuestas por mes (filtrado por anio de graduacion)
    Map<String, Long> responsesByMonth =
      surveyStatisticsRepositoryPort.findAllSurveyResponses().stream()
        .filter(response -> response.submittedAt() != null)
            .filter(
                response ->
                    graduationYear == null
              || (response.graduationYear() != null
                && response.graduationYear() == graduationYear))
            .collect(
                Collectors.groupingBy(
            response -> response.submittedAt().getMonth().toString(),
                    Collectors.counting()));

    String chartTitle =
        graduationYear != null
            ? "Respuestas por Mes (Graduados " + graduationYear + ")"
            : "Respuestas por Mes";

    charts.add(
        ChartDataResponse.builder()
            .chartType("line")
            .title(chartTitle)
            .labels(new ArrayList<>(responsesByMonth.keySet()))
            .datasets(
                Arrays.asList(
                    ChartDataResponse.ChartDataset.builder()
                      .label(LABEL_RESPONSES)
                        .data(responsesByMonth.values().stream().collect(Collectors.toList()))
                      .borderColor(COLOR_BLUE)
                        .backgroundColor("rgba(33, 150, 243, 0.1)")
                        .build()))
                .configuration(createDefaultChartConfiguration())
            .build());

    return charts;
  }

  private List<DashboardOverviewResponse.KpiIndicator> generateKpiIndicators(
      Double responseRate, Long totalGraduates) {
    List<DashboardOverviewResponse.KpiIndicator> kpis = new ArrayList<>();

    String responseTrend = buildResponseTrend(responseRate);
    String responseColor = buildResponseColor(responseRate);

    kpis.add(
        DashboardOverviewResponse.KpiIndicator.builder()
            .name("Tasa de Respuesta Global")
            .value(String.format("%.1f%%", responseRate))
            .unit("%")
        .trend(responseTrend)
            .changePercentage(0.0) // Aqui podrias calcular el cambio vs periodo anterior
            .description("Porcentaje de graduados que han respondido encuestas")
        .color(responseColor)
            .build());

    Long activeSurveys = surveyStatisticsRepositoryPort.countSurveysByStatus(SurveyStatus.ACTIVE);
    kpis.add(
        DashboardOverviewResponse.KpiIndicator.builder()
            .name("Encuestas Activas")
            .value(activeSurveys.toString())
            .unit("encuestas")
            .trend("stable")
            .changePercentage(0.0)
            .description("Numero de encuestas actualmente abiertas")
            .color(COLOR_BLUE)
            .build());

    kpis.add(
        DashboardOverviewResponse.KpiIndicator.builder()
            .name("Total Graduados")
            .value(totalGraduates.toString())
            .unit("graduados")
            .trend("up")
            .changePercentage(0.0)
            .description("Numero total de graduados activos")
            .color("#9C27B0")
            .build());

    return kpis;
  }

  private List<SurveyResponse> getRecentSurveys() {
    return surveyStatisticsRepositoryPort.findRecentSurveyIds(5).stream()
      .map(surveyRepositoryPort::getDomain)
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

  private String getDemographicValue(
      SurveyStatisticsRepositoryPort.SurveyResponseData response, String demographic) {
    switch (demographic.toLowerCase()) {
      case "gender":
      case "genero":
        return response.gender() != null
            ? response.gender()
            : "No especificado";
      case "location":
      case "ubicacion":
        return response.departamento() != null && !response.departamento().isBlank()
            ? response.departamento()
            : "No especificado";
      case "industry":
      case "industria":
        return "Industria por definir"; // Placeholder
      case "employment":
      case "empleo":
        if (response.hasCurrentJob() != null && response.hasCurrentJob()) {
          return "Empleado";
        } else if (response.hasCurrentJob() != null) {
          return "Desempleado";
        }
        return "Sin informacion";
      default:
        return "Sin categoria";
    }
  }

  private String buildResponseTrend(Double responseRate) {
    if (responseRate > 70) {
      return "up";
    }
    if (responseRate > 50) {
      return "stable";
    }
    return "down";
  }

  private String buildResponseColor(Double responseRate) {
    if (responseRate > 70) {
      return COLOR_GREEN;
    }
    if (responseRate > 50) {
      return COLOR_ORANGE;
    }
    return COLOR_RED;
  }
}
