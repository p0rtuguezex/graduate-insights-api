package pe.com.graduate.insights.api.features.surveystatistics.application.usecase;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
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
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.survey.application.dto.QuestionResponse;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyResponse;
import pe.com.graduate.insights.api.features.survey.application.ports.output.SurveyRepositoryPort;
import pe.com.graduate.insights.api.features.survey.domain.model.SurveyStatus;
import pe.com.graduate.insights.api.features.surveystatistics.application.dto.ChartDataResponse;
import pe.com.graduate.insights.api.features.surveystatistics.application.dto.DashboardOverviewResponse;
import pe.com.graduate.insights.api.features.surveystatistics.application.dto.QuestionStatistics;
import pe.com.graduate.insights.api.features.surveystatistics.application.dto.SurveyStatisticsResponse;
import pe.com.graduate.insights.api.features.surveystatistics.application.ports.input.SurveyStatisticsUseCase;
import pe.com.graduate.insights.api.features.surveystatistics.domain.port.output.SurveyStatisticsRepositoryPort;

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
    List<QuestionStatistics> questionStats =
        generateQuestionStatisticsFromDb(survey.getQuestions());

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
        surveyStatisticsRepositoryPort.findQuestionResponsesBySurveyIdAndQuestionId(
            surveyId, questionId);

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
    Long totalResponses =
        surveyStatisticsRepositoryPort.countQuestionResponsesByQuestionId(questionId);

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
            .completedSurveys(
                surveyStatisticsRepositoryPort.countSurveysByStatus(SurveyStatus.COMPLETED))
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
    String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    try {
      switch (format.toLowerCase()) {
        case "excel":
        case "xlsx":
          return buildExcelReport(surveyId, ts);
        case "pdf":
          return buildPdfReport(surveyId, ts);
        default:
          return buildCsvReport(surveyId, ts);
      }
    } catch (Exception e) {
      log.error("Error exportando encuesta {}: {}", surveyId, e.getMessage(), e);
      throw new RuntimeException("Error al generar el reporte: " + e.getMessage());
    }
  }

  @Override
  public ResponseEntity<byte[]> exportGeneralReport(String format) {
    String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    try {
      switch (format.toLowerCase()) {
        case "excel":
        case "xlsx":
          return buildGeneralExcelReport(ts);
        case "pdf":
          return buildGeneralPdfReport(ts);
        default:
          return buildGeneralCsvReport(ts);
      }
    } catch (Exception e) {
      log.error("Error exportando reporte general: {}", e.getMessage(), e);
      throw new RuntimeException("Error al generar el reporte general: " + e.getMessage());
    }
  }

  // Metodos auxiliares para obtener datos reales de la base de datos

  private List<QuestionStatistics> generateQuestionStatisticsFromDb(
      List<QuestionResponse> questions) {
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
      case YES_NO:
        applyYesNoQuestionStatistics(builder, question.getId(), totalResponses);
        break;
      case SINGLE_CHOICE:
      case MULTIPLE_CHOICE:
        applyChoiceQuestionStatistics(builder, question.getId(), totalResponses);
        break;
      case SCALE:
        applyScaleQuestionStatistics(builder, question.getId(), totalResponses);
        break;
      case NUMBER:
        applyNumberQuestionStatistics(builder, question.getId(), totalResponses);
        break;
      case TEXT:
      case DATE:
      case EMAIL:
      case PHONE:
        applyTextQuestionStatistics(builder, question.getId());
        break;
      default:
        throw new IllegalArgumentException(
            "Tipo de pregunta no soportado: " + question.getQuestionType());
    }

    return builder.build();
  }

  private void applyYesNoQuestionStatistics(
      QuestionStatistics.QuestionStatisticsBuilder builder, Long questionId, Long totalResponses) {
    List<String> textResponses =
        surveyStatisticsRepositoryPort.findTextResponsesByQuestionId(questionId);

    Map<String, Long> rawCounts =
        textResponses.stream()
            .filter(r -> r != null && !r.isBlank())
            .collect(Collectors.groupingBy(r -> r.trim().toUpperCase(), Collectors.counting()));

    Map<String, Long> labeledCounts = new LinkedHashMap<>();
    if (rawCounts.containsKey("SI")) labeledCounts.put("Sí", rawCounts.get("SI"));
    if (rawCounts.containsKey("NO")) labeledCounts.put("No", rawCounts.get("NO"));
    rawCounts.forEach(
        (k, v) -> {
          if (!k.equals("SI") && !k.equals("NO")) labeledCounts.put(k, v);
        });

    Map<String, Double> percentages = calculatePercentages(labeledCounts, totalResponses);
    builder.optionCounts(labeledCounts).percentages(percentages).recommendedChartType("pie");
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

      double average =
          numericValues.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
      double min = numericValues.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
      double max = numericValues.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

      // Compute median
      Collections.sort(numericValues);
      int n = numericValues.size();
      double median =
          n % 2 == 0
              ? (numericValues.get(n / 2 - 1) + numericValues.get(n / 2)) / 2.0
              : numericValues.get(n / 2);

      // Compute mode
      Map<Double, Long> frequencyMap =
          numericValues.stream().collect(Collectors.groupingBy(v -> v, Collectors.counting()));
      double mode =
          frequencyMap.entrySet().stream()
              .max(Map.Entry.comparingByValue())
              .map(Map.Entry::getKey)
              .orElse(0.0);

      // Compute standard deviation
      double variance =
          numericValues.stream().mapToDouble(v -> Math.pow(v - average, 2)).average().orElse(0.0);
      double stddev = Math.sqrt(variance);

      builder
          .average(average)
          .min(min)
          .max(max)
          .median(median)
          .mode(mode)
          .standardDeviation(stddev);
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

    double average =
        numericResponses.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
    double min = numericResponses.stream().mapToDouble(Integer::doubleValue).min().orElse(0.0);
    double max = numericResponses.stream().mapToDouble(Integer::doubleValue).max().orElse(0.0);

    // Compute median
    List<Double> sortedValues =
        numericResponses.stream().map(Integer::doubleValue).sorted().collect(Collectors.toList());
    int n = sortedValues.size();
    double median =
        n % 2 == 0
            ? (sortedValues.get(n / 2 - 1) + sortedValues.get(n / 2)) / 2.0
            : sortedValues.get(n / 2);

    // Compute mode
    Map<Double, Long> frequencyMap =
        sortedValues.stream().collect(Collectors.groupingBy(v -> v, Collectors.counting()));
    double mode =
        frequencyMap.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(0.0);

    // Compute standard deviation
    double variance =
        sortedValues.stream().mapToDouble(v -> Math.pow(v - average, 2)).average().orElse(0.0);
    double stddev = Math.sqrt(variance);

    Map<String, Long> numberDistribution =
        numericResponses.stream()
            .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()));
    Map<String, Double> numberPercentages =
        calculatePercentages(numberDistribution, totalResponses);

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
                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

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
                response -> response.gender() != null ? response.gender() : "No especificado",
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

  // ==================== EXPORT: CSV ====================

  private ResponseEntity<byte[]> buildCsvReport(Long surveyId, String ts) throws Exception {
    SurveyStatisticsResponse s = getSurveyStatistics(surveyId);
    List<SurveyStatisticsRepositoryPort.QuestionResponseData> raw =
        surveyStatisticsRepositoryPort.findQuestionResponsesBySurveyId(surveyId);

    StringBuilder csv = new StringBuilder();
    csv.append('\uFEFF'); // UTF-8 BOM

    csv.append("REPORTE DE ESTADÍSTICAS DE ENCUESTA\n");
    csv.append("Generado el:,")
        .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
        .append("\n\n");

    csv.append("INFORMACIÓN GENERAL\n");
    csv.append("Título,").append(csvEsc(s.getSurveyTitle())).append("\n");
    csv.append("Descripción,").append(csvEsc(s.getSurveyDescription())).append("\n");
    csv.append("Estado,").append(rptTranslateStatus(s.getStatus())).append("\n");
    csv.append("Tipo,")
        .append(s.getSurveyType() != null ? csvEsc(s.getSurveyType().getName()) : "N/A")
        .append("\n");
    csv.append("Fecha Inicio,")
        .append(s.getStartDate() != null ? s.getStartDate().toString() : "N/A")
        .append("\n");
    csv.append("Fecha Fin,")
        .append(s.getEndDate() != null ? s.getEndDate().toString() : "N/A")
        .append("\n\n");

    csv.append("MÉTRICAS CLAVE\n");
    csv.append("Total Egresados,").append(s.getTotalGraduates()).append("\n");
    csv.append("Total Respuestas,").append(s.getTotalResponses()).append("\n");
    csv.append("Respuestas Pendientes,").append(s.getPendingResponses()).append("\n");
    csv.append("Tasa de Respuesta,")
        .append(String.format("%.1f%%", s.getResponseRate()))
        .append("\n");
    csv.append("Tasa de Completitud,")
        .append(String.format("%.1f%%", s.getCompletionRate()))
        .append("\n\n");

    if (s.getQuestionStatistics() != null && !s.getQuestionStatistics().isEmpty()) {
      csv.append("ANÁLISIS POR PREGUNTA\n");
      for (QuestionStatistics q : s.getQuestionStatistics()) {
        csv.append("Pregunta,").append(csvEsc(q.getQuestionText())).append("\n");
        csv.append("Tipo,").append(rptTranslateType(q.getType())).append("\n");
        csv.append("Requerida,")
            .append(Boolean.TRUE.equals(q.getRequired()) ? "Sí" : "No")
            .append("\n");
        csv.append("Total Respuestas,").append(q.getTotalResponses()).append("\n");
        if (q.getOptionCounts() != null && !q.getOptionCounts().isEmpty()) {
          csv.append("Opción,Cantidad,Porcentaje\n");
          q.getOptionCounts()
              .forEach(
                  (opt, cnt) -> {
                    double pct =
                        q.getPercentages() != null
                            ? q.getPercentages().getOrDefault(opt, 0.0)
                            : 0.0;
                    csv.append(csvEsc(opt))
                        .append(",")
                        .append(cnt)
                        .append(",")
                        .append(String.format("%.1f%%", pct))
                        .append("\n");
                  });
        }
        if (q.getAverage() != null)
          csv.append("Promedio,").append(String.format("%.2f", q.getAverage())).append("\n");
        if (q.getMedian() != null)
          csv.append("Mediana,").append(String.format("%.2f", q.getMedian())).append("\n");
        if (q.getStandardDeviation() != null)
          csv.append("Desv. Estándar,")
              .append(String.format("%.2f", q.getStandardDeviation()))
              .append("\n");
        csv.append("\n");
      }
    }

    csv.append("DATOS BRUTOS\n");
    csv.append("Pregunta,Tipo,Respuesta,Graduado,Fecha\n");
    for (SurveyStatisticsRepositoryPort.QuestionResponseData r : raw) {
      csv.append(csvEsc(r.questionText()))
          .append(",")
          .append(rptTranslateType(r.questionType()))
          .append(",")
          .append(csvEsc(rptGetAnswer(r)))
          .append(",")
          .append(csvEsc(r.graduateFullName()))
          .append(",")
          .append(
              r.submittedAt() != null
                  ? r.submittedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                  : "")
          .append("\n");
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
    headers.setContentDispositionFormData(
        "attachment", "reporte_encuesta_" + surveyId + "_" + ts + ".csv");
    return ResponseEntity.ok().headers(headers).body(csv.toString().getBytes("UTF-8"));
  }

  private ResponseEntity<byte[]> buildGeneralCsvReport(String ts) throws Exception {
    List<Long> allIds = surveyStatisticsRepositoryPort.findRecentSurveyIds(1000);
    long totalGraduates = surveyStatisticsRepositoryPort.countActiveGraduates();

    StringBuilder csv = new StringBuilder();
    csv.append('\uFEFF');
    csv.append("REPORTE GENERAL DE ENCUESTAS\n");
    csv.append("Generado el:,")
        .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
        .append("\n");
    csv.append("Total Egresados Activos:,").append(totalGraduates).append("\n");
    csv.append("Total Encuestas:,").append(allIds.size()).append("\n\n");

    csv.append(
        "ID,Título,Estado,Tipo,Fecha Inicio,Fecha Fin,Preguntas,Respuestas,Tasa Respuesta,Completitud\n");
    for (Long id : allIds) {
      try {
        SurveyStatisticsResponse s = getSurveyStatistics(id);
        csv.append(s.getSurveyId())
            .append(",")
            .append(csvEsc(s.getSurveyTitle()))
            .append(",")
            .append(rptTranslateStatus(s.getStatus()))
            .append(",")
            .append(s.getSurveyType() != null ? csvEsc(s.getSurveyType().getName()) : "N/A")
            .append(",")
            .append(s.getStartDate() != null ? s.getStartDate().toString() : "")
            .append(",")
            .append(s.getEndDate() != null ? s.getEndDate().toString() : "")
            .append(",")
            .append(s.getTotalQuestions())
            .append(",")
            .append(s.getTotalResponses())
            .append(",")
            .append(String.format("%.1f%%", s.getResponseRate()))
            .append(",")
            .append(String.format("%.1f%%", s.getCompletionRate()))
            .append("\n");
      } catch (Exception e) {
        log.warn("Skipping survey {} in general report: {}", id, e.getMessage());
      }
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
    headers.setContentDispositionFormData("attachment", "reporte_general_encuestas_" + ts + ".csv");
    return ResponseEntity.ok().headers(headers).body(csv.toString().getBytes("UTF-8"));
  }

  // ==================== EXPORT: EXCEL ====================

  private ResponseEntity<byte[]> buildExcelReport(Long surveyId, String ts) throws Exception {
    SurveyStatisticsResponse s = getSurveyStatistics(surveyId);
    List<SurveyStatisticsRepositoryPort.QuestionResponseData> raw =
        surveyStatisticsRepositoryPort.findQuestionResponsesBySurveyId(surveyId);

    try (XSSFWorkbook wb = new XSSFWorkbook()) {
      XSSFCellStyle titleSt =
          xlStyle(wb, new java.awt.Color(30, 58, 95), java.awt.Color.WHITE, 18, true, true);
      XSSFCellStyle sectionSt =
          xlStyle(wb, new java.awt.Color(41, 128, 185), java.awt.Color.WHITE, 11, true, false);
      XSSFCellStyle headerSt =
          xlStyle(wb, new java.awt.Color(30, 58, 95), java.awt.Color.WHITE, 10, true, true);
      XSSFCellStyle labelSt =
          xlStyle(
              wb,
              new java.awt.Color(240, 244, 248),
              new java.awt.Color(30, 58, 95),
              10,
              true,
              false);
      XSSFCellStyle valueSt =
          xlStyle(wb, java.awt.Color.WHITE, new java.awt.Color(50, 50, 50), 10, false, false);
      XSSFCellStyle altSt =
          xlStyle(
              wb,
              new java.awt.Color(235, 242, 250),
              new java.awt.Color(50, 50, 50),
              10,
              false,
              false);
      XSSFCellStyle kpi1St =
          xlStyle(wb, new java.awt.Color(41, 128, 185), java.awt.Color.WHITE, 20, true, true);
      XSSFCellStyle kpi2St =
          xlStyle(wb, new java.awt.Color(39, 174, 96), java.awt.Color.WHITE, 20, true, true);
      XSSFCellStyle kpi3St =
          xlStyle(wb, new java.awt.Color(243, 156, 18), java.awt.Color.WHITE, 20, true, true);
      XSSFCellStyle kpi4St =
          xlStyle(wb, new java.awt.Color(231, 76, 60), java.awt.Color.WHITE, 20, true, true);
      XSSFCellStyle kpiLblSt =
          xlStyle(
              wb, new java.awt.Color(240, 240, 240), new java.awt.Color(80, 80, 80), 9, true, true);
      XSSFCellStyle optSubSt =
          xlStyle(
              wb,
              new java.awt.Color(245, 248, 252),
              new java.awt.Color(80, 80, 80),
              9,
              false,
              false);
      XSSFCellStyle optHdrSt =
          xlStyle(
              wb,
              new java.awt.Color(200, 220, 240),
              new java.awt.Color(30, 58, 95),
              9,
              true,
              false);

      // ---- SHEET 1: Resumen ----
      XSSFSheet sh1 = wb.createSheet("Resumen General");
      sh1.setColumnWidth(0, 8000);
      sh1.setColumnWidth(1, 7000);
      sh1.setColumnWidth(2, 5000);
      sh1.setColumnWidth(3, 5000);

      XSSFRow titleRow = sh1.createRow(0);
      titleRow.setHeight((short) 1000);
      XSSFCell tc = titleRow.createCell(0);
      tc.setCellValue("REPORTE DE ESTADÍSTICAS DE ENCUESTA");
      tc.setCellStyle(titleSt);
      sh1.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

      XSSFRow subtitleRow = sh1.createRow(1);
      subtitleRow.setHeight((short) 700);
      XSSFCell sc2 = subtitleRow.createCell(0);
      sc2.setCellValue(s.getSurveyTitle() != null ? s.getSurveyTitle() : "");
      sc2.setCellStyle(sectionSt);
      sh1.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));

      sh1.createRow(2);

      int row = 3;
      row = xlSection(sh1, row, "INFORMACIÓN GENERAL", sectionSt, 4);
      String[][] info = {
        {"Título", s.getSurveyTitle() != null ? s.getSurveyTitle() : "N/A"},
        {"Estado", rptTranslateStatus(s.getStatus())},
        {"Tipo de Encuesta", s.getSurveyType() != null ? s.getSurveyType().getName() : "N/A"},
        {"Fecha de Inicio", s.getStartDate() != null ? s.getStartDate().toString() : "N/A"},
        {"Fecha de Fin", s.getEndDate() != null ? s.getEndDate().toString() : "N/A"},
        {"Total de Preguntas", String.valueOf(s.getTotalQuestions())},
        {
          "Generado el", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        },
      };
      boolean alt = false;
      for (String[] pair : info) {
        XSSFRow r = sh1.createRow(row++);
        r.setHeight((short) 450);
        xlCell(r, 0, pair[0], labelSt);
        xlCell(r, 1, pair[1], alt ? altSt : valueSt);
        sh1.addMergedRegion(new CellRangeAddress(row - 1, row - 1, 1, 3));
        alt = !alt;
      }

      sh1.createRow(row++);
      row = xlSection(sh1, row, "MÉTRICAS CLAVE", sectionSt, 4);

      XSSFRow kpiLblRow = sh1.createRow(row++);
      XSSFRow kpiValRow = sh1.createRow(row++);
      kpiValRow.setHeight((short) 1400);
      String[] kpiLbls = {"Total Respuestas", "Tasa de Respuesta", "Completitud", "Pendientes"};
      String[] kpiVals = {
        String.valueOf(s.getTotalResponses()),
        String.format("%.1f%%", s.getResponseRate()),
        String.format("%.1f%%", s.getCompletionRate()),
        String.valueOf(s.getPendingResponses())
      };
      XSSFCellStyle[] kpiStyles = {kpi1St, kpi2St, kpi3St, kpi4St};
      for (int i = 0; i < 4; i++) {
        xlCell(kpiLblRow, i, kpiLbls[i], kpiLblSt);
        xlCell(kpiValRow, i, kpiVals[i], kpiStyles[i]);
      }

      sh1.createRow(row++);

      if (s.getResponsesByGender() != null && !s.getResponsesByGender().isEmpty()) {
        row = xlSection(sh1, row, "DISTRIBUCIÓN POR GÉNERO", sectionSt, 4);
        XSSFRow hRow = sh1.createRow(row++);
        xlCell(hRow, 0, "Género", headerSt);
        xlCell(hRow, 1, "Respuestas", headerSt);
        xlCell(hRow, 2, "Porcentaje", headerSt);
        long tot = s.getResponsesByGender().values().stream().mapToLong(Long::longValue).sum();
        alt = false;
        for (Map.Entry<String, Long> e : s.getResponsesByGender().entrySet()) {
          XSSFRow r = sh1.createRow(row++);
          XSSFCellStyle st = alt ? altSt : valueSt;
          xlCell(r, 0, rptTranslateGender(e.getKey()), st);
          xlCell(r, 1, String.valueOf(e.getValue()), st);
          xlCell(r, 2, tot > 0 ? String.format("%.1f%%", e.getValue() * 100.0 / tot) : "0%", st);
          alt = !alt;
        }
        sh1.createRow(row++);
      }

      if (s.getResponsesByMonth() != null && !s.getResponsesByMonth().isEmpty()) {
        row = xlSection(sh1, row, "RESPUESTAS POR MES", sectionSt, 4);
        XSSFRow hRow = sh1.createRow(row++);
        xlCell(hRow, 0, "Mes", headerSt);
        xlCell(hRow, 1, "Respuestas", headerSt);
        alt = false;
        for (Map.Entry<String, Long> e : s.getResponsesByMonth().entrySet()) {
          XSSFRow r = sh1.createRow(row++);
          XSSFCellStyle st = alt ? altSt : valueSt;
          xlCell(r, 0, rptTranslateMonth(e.getKey()), st);
          xlCell(r, 1, String.valueOf(e.getValue()), st);
          alt = !alt;
        }
      }

      // ---- SHEET 2: Questions ----
      XSSFSheet sh2 = wb.createSheet("Análisis por Pregunta");
      sh2.setColumnWidth(0, 14000);
      sh2.setColumnWidth(1, 4500);
      sh2.setColumnWidth(2, 3500);
      sh2.setColumnWidth(3, 4000);
      sh2.setColumnWidth(4, 4000);
      sh2.setColumnWidth(5, 4000);
      sh2.setColumnWidth(6, 4000);
      sh2.setColumnWidth(7, 4500);

      XSSFRow sh2T = sh2.createRow(0);
      sh2T.setHeight((short) 900);
      XSSFCell sh2Tc = sh2T.createCell(0);
      sh2Tc.setCellValue(
          "ANÁLISIS POR PREGUNTA — " + (s.getSurveyTitle() != null ? s.getSurveyTitle() : ""));
      sh2Tc.setCellStyle(titleSt);
      sh2.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
      sh2.createRow(1);

      XSSFRow qHdr = sh2.createRow(2);
      qHdr.setHeight((short) 600);
      String[] qh = {
        "Pregunta", "Tipo", "Requerida", "Respuestas", "Tasa", "Promedio", "Mediana", "Desv. Est."
      };
      for (int i = 0; i < qh.length; i++) xlCell(qHdr, i, qh[i], headerSt);

      int qr = 3;
      if (s.getQuestionStatistics() != null) {
        alt = false;
        for (QuestionStatistics q : s.getQuestionStatistics()) {
          XSSFCellStyle st = alt ? altSt : valueSt;
          XSSFRow qRow = sh2.createRow(qr++);
          qRow.setHeight((short) 500);
          xlCell(qRow, 0, q.getQuestionText() != null ? q.getQuestionText() : "", st);
          xlCell(qRow, 1, rptTranslateType(q.getType()), st);
          xlCell(qRow, 2, Boolean.TRUE.equals(q.getRequired()) ? "Sí" : "No", st);
          xlCell(
              qRow, 3, q.getTotalResponses() != null ? q.getTotalResponses().toString() : "0", st);
          xlCell(
              qRow,
              4,
              q.getResponseRate() != null ? String.format("%.1f%%", q.getResponseRate()) : "0%",
              st);
          xlCell(qRow, 5, q.getAverage() != null ? String.format("%.2f", q.getAverage()) : "-", st);
          xlCell(qRow, 6, q.getMedian() != null ? String.format("%.2f", q.getMedian()) : "-", st);
          xlCell(
              qRow,
              7,
              q.getStandardDeviation() != null
                  ? String.format("%.2f", q.getStandardDeviation())
                  : "-",
              st);
          alt = !alt;
          if (q.getOptionCounts() != null && !q.getOptionCounts().isEmpty()) {
            XSSFRow oh = sh2.createRow(qr++);
            xlCell(oh, 1, "  Opción", optHdrSt);
            xlCell(oh, 2, "Cantidad", optHdrSt);
            xlCell(oh, 3, "Porcentaje", optHdrSt);
            for (Map.Entry<String, Long> oe : q.getOptionCounts().entrySet()) {
              double pct =
                  q.getPercentages() != null
                      ? q.getPercentages().getOrDefault(oe.getKey(), 0.0)
                      : 0.0;
              XSSFRow or2 = sh2.createRow(qr++);
              xlCell(or2, 1, "  " + oe.getKey(), optSubSt);
              xlCell(or2, 2, String.valueOf(oe.getValue()), optSubSt);
              xlCell(or2, 3, String.format("%.1f%%", pct), optSubSt);
            }
            sh2.createRow(qr++);
          }
        }
      }

      // ---- SHEET 3: Raw Data ----
      XSSFSheet sh3 = wb.createSheet("Respuestas Detalladas");
      sh3.setColumnWidth(0, 12000);
      sh3.setColumnWidth(1, 4000);
      sh3.setColumnWidth(2, 12000);
      sh3.setColumnWidth(3, 6000);
      sh3.setColumnWidth(4, 5500);
      sh3.setColumnWidth(5, 3500);

      XSSFRow sh3T = sh3.createRow(0);
      sh3T.setHeight((short) 900);
      XSSFCell sh3Tc = sh3T.createCell(0);
      sh3Tc.setCellValue("RESPUESTAS DETALLADAS");
      sh3Tc.setCellStyle(titleSt);
      sh3.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
      sh3.createRow(1);

      XSSFRow rawHdr = sh3.createRow(2);
      rawHdr.setHeight((short) 600);
      String[] rh = {"Pregunta", "Tipo", "Respuesta", "Graduado", "Fecha", "Género"};
      for (int i = 0; i < rh.length; i++) xlCell(rawHdr, i, rh[i], headerSt);

      int rr = 3;
      alt = false;
      for (SurveyStatisticsRepositoryPort.QuestionResponseData rd : raw) {
        XSSFCellStyle st = alt ? altSt : valueSt;
        XSSFRow rRow = sh3.createRow(rr++);
        xlCell(rRow, 0, rd.questionText() != null ? rd.questionText() : "", st);
        xlCell(rRow, 1, rptTranslateType(rd.questionType()), st);
        xlCell(rRow, 2, rptGetAnswer(rd), st);
        xlCell(rRow, 3, rd.graduateFullName() != null ? rd.graduateFullName() : "", st);
        xlCell(
            rRow,
            4,
            rd.submittedAt() != null
                ? rd.submittedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : "",
            st);
        xlCell(rRow, 5, rptTranslateGender(rd.gender()), st);
        alt = !alt;
      }

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      wb.write(out);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(
          MediaType.parseMediaType(
              "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      headers.setContentDispositionFormData(
          "attachment", "reporte_encuesta_" + surveyId + "_" + ts + ".xlsx");
      return ResponseEntity.ok().headers(headers).body(out.toByteArray());
    }
  }

  private ResponseEntity<byte[]> buildGeneralExcelReport(String ts) throws Exception {
    List<Long> allIds = surveyStatisticsRepositoryPort.findRecentSurveyIds(1000);
    long totalGraduates = surveyStatisticsRepositoryPort.countActiveGraduates();

    try (XSSFWorkbook wb = new XSSFWorkbook()) {
      XSSFCellStyle titleSt =
          xlStyle(wb, new java.awt.Color(30, 58, 95), java.awt.Color.WHITE, 18, true, true);
      XSSFCellStyle sectionSt =
          xlStyle(wb, new java.awt.Color(41, 128, 185), java.awt.Color.WHITE, 11, true, false);
      XSSFCellStyle headerSt =
          xlStyle(wb, new java.awt.Color(30, 58, 95), java.awt.Color.WHITE, 10, true, true);
      XSSFCellStyle valueSt =
          xlStyle(wb, java.awt.Color.WHITE, new java.awt.Color(50, 50, 50), 10, false, false);
      XSSFCellStyle altSt =
          xlStyle(
              wb,
              new java.awt.Color(235, 242, 250),
              new java.awt.Color(50, 50, 50),
              10,
              false,
              false);
      XSSFCellStyle labelSt =
          xlStyle(
              wb,
              new java.awt.Color(240, 244, 248),
              new java.awt.Color(30, 58, 95),
              10,
              true,
              false);

      XSSFSheet sh = wb.createSheet("Resumen General");
      sh.setColumnWidth(0, 2000);
      sh.setColumnWidth(1, 10000);
      sh.setColumnWidth(2, 4000);
      sh.setColumnWidth(3, 5000);
      sh.setColumnWidth(4, 4500);
      sh.setColumnWidth(5, 4500);
      sh.setColumnWidth(6, 3500);
      sh.setColumnWidth(7, 4000);
      sh.setColumnWidth(8, 4500);
      sh.setColumnWidth(9, 4500);

      XSSFRow tR = sh.createRow(0);
      tR.setHeight((short) 900);
      XSSFCell tC = tR.createCell(0);
      tC.setCellValue("REPORTE GENERAL DE ENCUESTAS — EgreSys");
      tC.setCellStyle(titleSt);
      sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

      sh.createRow(1);
      int row = 2;
      row = xlSection(sh, row, "RESUMEN DEL SISTEMA", sectionSt, 10);
      String[][] summary = {
        {"Total de Encuestas", String.valueOf(allIds.size())},
        {"Total de Egresados Activos", String.valueOf(totalGraduates)},
        {
          "Generado el", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        },
      };
      for (String[] pair : summary) {
        XSSFRow r = sh.createRow(row++);
        r.setHeight((short) 450);
        xlCell(r, 0, pair[0], labelSt);
        xlCell(r, 1, pair[1], valueSt);
        sh.addMergedRegion(new CellRangeAddress(row - 1, row - 1, 1, 9));
      }

      sh.createRow(row++);
      row = xlSection(sh, row, "DETALLE POR ENCUESTA", sectionSt, 10);

      XSSFRow hRow = sh.createRow(row++);
      hRow.setHeight((short) 600);
      String[] cols = {
        "ID",
        "Título",
        "Estado",
        "Tipo",
        "Fecha Inicio",
        "Fecha Fin",
        "Preguntas",
        "Respuestas",
        "Tasa Resp.",
        "Completitud"
      };
      for (int i = 0; i < cols.length; i++) xlCell(hRow, i, cols[i], headerSt);

      boolean alt = false;
      for (Long id : allIds) {
        try {
          SurveyStatisticsResponse s = getSurveyStatistics(id);
          XSSFCellStyle st = alt ? altSt : valueSt;
          XSSFRow r = sh.createRow(row++);
          r.setHeight((short) 450);
          xlCell(r, 0, String.valueOf(s.getSurveyId()), st);
          xlCell(r, 1, s.getSurveyTitle() != null ? s.getSurveyTitle() : "", st);
          xlCell(r, 2, rptTranslateStatus(s.getStatus()), st);
          xlCell(r, 3, s.getSurveyType() != null ? s.getSurveyType().getName() : "N/A", st);
          xlCell(r, 4, s.getStartDate() != null ? s.getStartDate().toString() : "", st);
          xlCell(r, 5, s.getEndDate() != null ? s.getEndDate().toString() : "", st);
          xlCell(r, 6, String.valueOf(s.getTotalQuestions()), st);
          xlCell(r, 7, String.valueOf(s.getTotalResponses()), st);
          xlCell(r, 8, String.format("%.1f%%", s.getResponseRate()), st);
          xlCell(r, 9, String.format("%.1f%%", s.getCompletionRate()), st);
          alt = !alt;
        } catch (Exception e) {
          log.warn("Skipping survey {} in general Excel report: {}", id, e.getMessage());
        }
      }

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      wb.write(out);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(
          MediaType.parseMediaType(
              "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      headers.setContentDispositionFormData(
          "attachment", "reporte_general_encuestas_" + ts + ".xlsx");
      return ResponseEntity.ok().headers(headers).body(out.toByteArray());
    }
  }

  // ==================== EXPORT: PDF ====================

  private ResponseEntity<byte[]> buildPdfReport(Long surveyId, String ts) throws Exception {
    SurveyStatisticsResponse s = getSurveyStatistics(surveyId);
    List<SurveyStatisticsRepositoryPort.QuestionResponseData> raw =
        surveyStatisticsRepositoryPort.findQuestionResponsesBySurveyId(surveyId);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document doc = new Document(PageSize.A4, 40, 40, 75, 55);
    PdfWriter writer = PdfWriter.getInstance(doc, out);
    writer.setPageEvent(
        new RptPageEvent("EgreSys — " + (s.getSurveyTitle() != null ? s.getSurveyTitle() : "")));
    doc.open();

    Color NAVY = new Color(30, 58, 95);
    Color BLUE = new Color(41, 128, 185);
    Color GREEN = new Color(39, 174, 96);
    Color ORANGE = new Color(243, 156, 18);
    Color RED = new Color(231, 76, 60);
    Color LIGHT = new Color(235, 242, 250);
    Color WHITE = Color.WHITE;
    Color GRAY = new Color(248, 249, 250);
    Color BORDER = new Color(200, 210, 220);
    Color DARK = new Color(50, 50, 50);
    Color MUTED = new Color(100, 100, 100);

    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, WHITE);
    Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, WHITE);
    Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, NAVY);
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, WHITE);
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, DARK);
    Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, DARK);
    Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8, MUTED);
    Font smallBoldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, MUTED);
    Font kpiFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, WHITE);
    Font kpiLblFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, WHITE);

    // Banner
    PdfPTable banner = new PdfPTable(1);
    banner.setWidthPercentage(100);
    PdfPCell bannerCell = new PdfPCell();
    bannerCell.setBackgroundColor(NAVY);
    bannerCell.setPaddingTop(22);
    bannerCell.setPaddingBottom(22);
    bannerCell.setPaddingLeft(20);
    bannerCell.setPaddingRight(20);
    bannerCell.setBorder(Rectangle.NO_BORDER);
    Paragraph bannerText = new Paragraph();
    bannerText.setAlignment(Element.ALIGN_LEFT);
    bannerText.add(new Chunk("REPORTE DE ESTADÍSTICAS\n", titleFont));
    bannerText.add(new Chunk("DE ENCUESTA\n\n", titleFont));
    bannerText.add(new Chunk(s.getSurveyTitle() != null ? s.getSurveyTitle() : "", subtitleFont));
    bannerCell.addElement(bannerText);
    banner.addCell(bannerCell);
    doc.add(banner);

    pdfSection(doc, "INFORMACIÓN GENERAL", sectionFont, BLUE);
    PdfPTable infoTbl = new PdfPTable(2);
    infoTbl.setWidthPercentage(100);
    infoTbl.setWidths(new float[] {1.5f, 3f});
    infoTbl.setSpacingBefore(6);
    String[][] info = {
      {"Estado", rptTranslateStatus(s.getStatus())},
      {"Tipo de Encuesta", s.getSurveyType() != null ? s.getSurveyType().getName() : "N/A"},
      {"Descripción", s.getSurveyDescription() != null ? s.getSurveyDescription() : "N/A"},
      {"Fecha de Inicio", s.getStartDate() != null ? s.getStartDate().toString() : "N/A"},
      {"Fecha de Fin", s.getEndDate() != null ? s.getEndDate().toString() : "N/A"},
      {"Total de Preguntas", String.valueOf(s.getTotalQuestions())},
    };
    for (int i = 0; i < info.length; i++) {
      pdfCell(infoTbl, info[i][0], boldFont, i % 2 == 0 ? LIGHT : GRAY, BORDER, true);
      pdfCell(
          infoTbl,
          info[i][1],
          normalFont,
          i % 2 == 0 ? WHITE : new Color(252, 252, 252),
          BORDER,
          false);
    }
    doc.add(infoTbl);

    doc.add(Chunk.NEWLINE);
    pdfSection(doc, "MÉTRICAS CLAVE", sectionFont, BLUE);
    PdfPTable kpiTbl = new PdfPTable(4);
    kpiTbl.setWidthPercentage(100);
    kpiTbl.setSpacingBefore(5);
    pdfKpi(
        kpiTbl,
        String.valueOf(s.getTotalResponses()),
        "Total Respuestas",
        BLUE,
        kpiFont,
        kpiLblFont);
    pdfKpi(
        kpiTbl,
        String.format("%.1f%%", s.getResponseRate()),
        "Tasa de Respuesta",
        GREEN,
        kpiFont,
        kpiLblFont);
    pdfKpi(
        kpiTbl,
        String.format("%.1f%%", s.getCompletionRate()),
        "Completitud",
        ORANGE,
        kpiFont,
        kpiLblFont);
    pdfKpi(kpiTbl, String.valueOf(s.getPendingResponses()), "Pendientes", RED, kpiFont, kpiLblFont);
    doc.add(kpiTbl);

    if (s.getResponsesByGender() != null && !s.getResponsesByGender().isEmpty()) {
      doc.add(Chunk.NEWLINE);
      pdfSection(doc, "DISTRIBUCIÓN POR GÉNERO", sectionFont, BLUE);
      PdfPTable gTbl = new PdfPTable(3);
      gTbl.setWidthPercentage(55);
      gTbl.setHorizontalAlignment(Element.ALIGN_LEFT);
      gTbl.setSpacingBefore(5);
      for (String h : new String[] {"Género", "Respuestas", "Porcentaje"})
        pdfHeaderCell(gTbl, h, headerFont, NAVY);
      long tot = s.getResponsesByGender().values().stream().mapToLong(Long::longValue).sum();
      boolean alt = false;
      for (Map.Entry<String, Long> e : s.getResponsesByGender().entrySet()) {
        Color bg = alt ? LIGHT : WHITE;
        pdfCell(gTbl, rptTranslateGender(e.getKey()), normalFont, bg, BORDER, false);
        pdfCell(gTbl, String.valueOf(e.getValue()), normalFont, bg, BORDER, false);
        pdfCell(
            gTbl,
            tot > 0 ? String.format("%.1f%%", e.getValue() * 100.0 / tot) : "0%",
            normalFont,
            bg,
            BORDER,
            false);
        alt = !alt;
      }
      doc.add(gTbl);
    }

    if (s.getResponsesByMonth() != null && !s.getResponsesByMonth().isEmpty()) {
      doc.add(Chunk.NEWLINE);
      pdfSection(doc, "RESPUESTAS POR MES", sectionFont, BLUE);
      PdfPTable mTbl = new PdfPTable(2);
      mTbl.setWidthPercentage(45);
      mTbl.setHorizontalAlignment(Element.ALIGN_LEFT);
      mTbl.setSpacingBefore(5);
      for (String h : new String[] {"Mes", "Respuestas"}) pdfHeaderCell(mTbl, h, headerFont, NAVY);
      boolean alt = false;
      for (Map.Entry<String, Long> e : s.getResponsesByMonth().entrySet()) {
        Color bg = alt ? LIGHT : WHITE;
        pdfCell(mTbl, rptTranslateMonth(e.getKey()), normalFont, bg, BORDER, false);
        pdfCell(mTbl, String.valueOf(e.getValue()), normalFont, bg, BORDER, false);
        alt = !alt;
      }
      doc.add(mTbl);
    }

    if (s.getQuestionStatistics() != null && !s.getQuestionStatistics().isEmpty()) {
      doc.newPage();
      pdfSection(doc, "ANÁLISIS POR PREGUNTA", sectionFont, BLUE);
      for (int qi = 0; qi < s.getQuestionStatistics().size(); qi++) {
        QuestionStatistics q = s.getQuestionStatistics().get(qi);
        Paragraph qTitle = new Paragraph();
        qTitle.add(
            new Chunk(
                (qi + 1) + ". " + (q.getQuestionText() != null ? q.getQuestionText() : ""),
                boldFont));
        qTitle.setSpacingBefore(12);
        qTitle.setSpacingAfter(4);
        doc.add(qTitle);

        PdfPTable metaTbl = new PdfPTable(4);
        metaTbl.setWidthPercentage(100);
        for (String lbl : new String[] {"Tipo", "Requerida", "Respuestas", "Tasa"})
          pdfHeaderCell(metaTbl, lbl, smallBoldFont, LIGHT);
        pdfDataCell(metaTbl, rptTranslateType(q.getType()), normalFont, WHITE, BORDER);
        pdfDataCell(
            metaTbl, Boolean.TRUE.equals(q.getRequired()) ? "Sí" : "No", normalFont, WHITE, BORDER);
        pdfDataCell(
            metaTbl,
            q.getTotalResponses() != null ? q.getTotalResponses().toString() : "0",
            normalFont,
            WHITE,
            BORDER);
        pdfDataCell(
            metaTbl,
            q.getResponseRate() != null ? String.format("%.1f%%", q.getResponseRate()) : "0%",
            normalFont,
            WHITE,
            BORDER);
        doc.add(metaTbl);

        if (q.getOptionCounts() != null && !q.getOptionCounts().isEmpty()) {
          PdfPTable optTbl = new PdfPTable(3);
          optTbl.setWidthPercentage(75);
          optTbl.setWidths(new float[] {3f, 1f, 1.2f});
          optTbl.setHorizontalAlignment(Element.ALIGN_LEFT);
          optTbl.setSpacingBefore(4);
          for (String h : new String[] {"Opción", "Cantidad", "Porcentaje"})
            pdfHeaderCell(optTbl, h, headerFont, BLUE);
          boolean alt = false;
          for (Map.Entry<String, Long> e : q.getOptionCounts().entrySet()) {
            double pct =
                q.getPercentages() != null ? q.getPercentages().getOrDefault(e.getKey(), 0.0) : 0.0;
            Color bg = alt ? LIGHT : WHITE;
            pdfCell(optTbl, e.getKey(), normalFont, bg, BORDER, false);
            pdfCell(optTbl, String.valueOf(e.getValue()), normalFont, bg, BORDER, false);
            pdfCell(optTbl, String.format("%.1f%%", pct), normalFont, bg, BORDER, false);
            alt = !alt;
          }
          doc.add(optTbl);
        }

        if (q.getAverage() != null || q.getMedian() != null) {
          PdfPTable statsTbl = new PdfPTable(4);
          statsTbl.setWidthPercentage(75);
          statsTbl.setHorizontalAlignment(Element.ALIGN_LEFT);
          statsTbl.setSpacingBefore(4);
          for (String lbl : new String[] {"Promedio", "Mediana", "Moda", "Desv. Estándar"}) {
            PdfPCell c = new PdfPCell(new Phrase(lbl, smallBoldFont));
            c.setBackgroundColor(new Color(220, 235, 245));
            c.setPadding(4);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setBorderColor(BORDER);
            statsTbl.addCell(c);
          }
          for (Double v :
              new Double[] {q.getAverage(), q.getMedian(), q.getMode(), q.getStandardDeviation()}) {
            PdfPCell c =
                new PdfPCell(new Phrase(v != null ? String.format("%.2f", v) : "—", boldFont));
            c.setPadding(4);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setBorderColor(BORDER);
            statsTbl.addCell(c);
          }
          doc.add(statsTbl);
        }
      }
    }

    doc.close();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData(
        "attachment", "reporte_encuesta_" + surveyId + "_" + ts + ".pdf");
    return ResponseEntity.ok().headers(headers).body(out.toByteArray());
  }

  private ResponseEntity<byte[]> buildGeneralPdfReport(String ts) throws Exception {
    List<Long> allIds = surveyStatisticsRepositoryPort.findRecentSurveyIds(1000);
    long totalGraduates = surveyStatisticsRepositoryPort.countActiveGraduates();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document doc = new Document(PageSize.A4.rotate(), 40, 40, 75, 55);
    PdfWriter writer = PdfWriter.getInstance(doc, out);
    writer.setPageEvent(new RptPageEvent("EgreSys — Reporte General de Encuestas"));
    doc.open();

    Color NAVY = new Color(30, 58, 95);
    Color BLUE = new Color(41, 128, 185);
    Color LIGHT = new Color(235, 242, 250);
    Color WHITE = Color.WHITE;
    Color BORDER = new Color(200, 210, 220);
    Color DARK = new Color(50, 50, 50);

    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, WHITE);
    Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, WHITE);
    Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, NAVY);
    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, WHITE);
    Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, DARK);
    Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9, DARK);
    Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 8, new Color(100, 100, 100));

    // Banner
    PdfPTable banner = new PdfPTable(1);
    banner.setWidthPercentage(100);
    PdfPCell bannerCell = new PdfPCell();
    bannerCell.setBackgroundColor(NAVY);
    bannerCell.setPaddingTop(20);
    bannerCell.setPaddingBottom(20);
    bannerCell.setPaddingLeft(20);
    bannerCell.setBorder(Rectangle.NO_BORDER);
    Paragraph bp = new Paragraph();
    bp.add(new Chunk("REPORTE GENERAL DE ENCUESTAS\n", titleFont));
    bp.add(new Chunk("Sistema de Gestión de Egresados — EgreSys\n\n", subtitleFont));
    bp.add(
        new Chunk(
            "Total de encuestas: "
                + allIds.size()
                + "   |   Egresados activos: "
                + totalGraduates
                + "   |   Generado: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(180, 200, 220))));
    bannerCell.addElement(bp);
    banner.addCell(bannerCell);
    doc.add(banner);

    pdfSection(doc, "DETALLE POR ENCUESTA", sectionFont, BLUE);

    PdfPTable tbl = new PdfPTable(9);
    tbl.setWidthPercentage(100);
    tbl.setWidths(new float[] {0.5f, 2.5f, 1f, 1.5f, 1.2f, 1.2f, 0.8f, 0.9f, 1f});
    tbl.setSpacingBefore(6);
    for (String h :
        new String[] {
          "ID", "Título", "Estado", "Tipo", "Inicio", "Fin", "Preg.", "Resp.", "Tasa Resp."
        }) {
      pdfHeaderCell(tbl, h, headerFont, NAVY);
    }

    boolean alt = false;
    for (Long id : allIds) {
      try {
        SurveyStatisticsResponse s = getSurveyStatistics(id);
        Color bg = alt ? LIGHT : WHITE;
        pdfCell(tbl, String.valueOf(s.getSurveyId()), normalFont, bg, BORDER, false);
        pdfCell(
            tbl,
            s.getSurveyTitle() != null ? s.getSurveyTitle() : "",
            normalFont,
            bg,
            BORDER,
            false);
        pdfCell(tbl, rptTranslateStatus(s.getStatus()), normalFont, bg, BORDER, false);
        pdfCell(
            tbl,
            s.getSurveyType() != null ? s.getSurveyType().getName() : "N/A",
            normalFont,
            bg,
            BORDER,
            false);
        pdfCell(
            tbl,
            s.getStartDate() != null ? s.getStartDate().toString() : "",
            normalFont,
            bg,
            BORDER,
            false);
        pdfCell(
            tbl,
            s.getEndDate() != null ? s.getEndDate().toString() : "",
            normalFont,
            bg,
            BORDER,
            false);
        pdfCell(tbl, String.valueOf(s.getTotalQuestions()), normalFont, bg, BORDER, false);
        pdfCell(tbl, String.valueOf(s.getTotalResponses()), normalFont, bg, BORDER, false);
        pdfCell(tbl, String.format("%.1f%%", s.getResponseRate()), normalFont, bg, BORDER, false);
        alt = !alt;
      } catch (Exception e) {
        log.warn("Skipping survey {} in general PDF report: {}", id, e.getMessage());
      }
    }
    doc.add(tbl);

    doc.close();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "reporte_general_encuestas_" + ts + ".pdf");
    return ResponseEntity.ok().headers(headers).body(out.toByteArray());
  }

  // ==================== PDF HELPERS ====================

  private static class RptPageEvent extends PdfPageEventHelper {
    private final String header;

    RptPageEvent(String header) {
      this.header = header;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
      try {
        Font f = FontFactory.getFont(FontFactory.HELVETICA, 7, new Color(130, 130, 130));
        PdfPTable footer = new PdfPTable(2);
        footer.setTotalWidth(document.right() - document.left());
        footer.setWidths(new float[] {4f, 1f});
        PdfPCell left = new PdfPCell(new Phrase(header, f));
        left.setBorderWidthBottom(0.5f);
        left.setBorderWidthTop(0);
        left.setBorderWidthLeft(0);
        left.setBorderWidthRight(0);
        left.setBorderColor(new Color(200, 210, 220));
        left.setPaddingBottom(4);
        PdfPCell right = new PdfPCell(new Phrase("Página " + writer.getPageNumber(), f));
        right.setBorderWidthBottom(0.5f);
        right.setBorderWidthTop(0);
        right.setBorderWidthLeft(0);
        right.setBorderWidthRight(0);
        right.setBorderColor(new Color(200, 210, 220));
        right.setPaddingBottom(4);
        right.setHorizontalAlignment(Element.ALIGN_RIGHT);
        footer.addCell(left);
        footer.addCell(right);
        footer.writeSelectedRows(
            0, -1, document.left(), document.bottom() - 5, writer.getDirectContent());
      } catch (Exception ignored) {
      }
    }
  }

  private void pdfSection(Document doc, String title, Font font, Color lineColor) throws Exception {
    Paragraph p = new Paragraph(title, font);
    p.setSpacingBefore(12);
    p.setSpacingAfter(4);
    doc.add(p);
    PdfPTable line = new PdfPTable(1);
    line.setWidthPercentage(100);
    PdfPCell lc = new PdfPCell();
    lc.setBorderWidthBottom(2f);
    lc.setBorderWidthTop(0);
    lc.setBorderWidthLeft(0);
    lc.setBorderWidthRight(0);
    lc.setBorderColor(lineColor);
    lc.setPaddingBottom(2);
    line.addCell(lc);
    doc.add(line);
  }

  private void pdfHeaderCell(PdfPTable tbl, String text, Font font, Color bg) {
    PdfPCell c = new PdfPCell(new Phrase(text, font));
    c.setBackgroundColor(bg);
    c.setPadding(6);
    c.setBorder(Rectangle.NO_BORDER);
    c.setHorizontalAlignment(Element.ALIGN_CENTER);
    tbl.addCell(c);
  }

  private void pdfCell(
      PdfPTable tbl, String text, Font font, Color bg, Color border, boolean bold) {
    Font f =
        bold
            ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, font.getSize(), font.getColor())
            : font;
    PdfPCell c = new PdfPCell(new Phrase(text != null ? text : "", f));
    c.setBackgroundColor(bg);
    c.setPadding(5);
    c.setBorderColor(border);
    tbl.addCell(c);
  }

  private void pdfDataCell(PdfPTable tbl, String text, Font font, Color bg, Color border) {
    PdfPCell c = new PdfPCell(new Phrase(text != null ? text : "", font));
    c.setBackgroundColor(bg);
    c.setPadding(4);
    c.setBorderColor(border);
    c.setHorizontalAlignment(Element.ALIGN_CENTER);
    tbl.addCell(c);
  }

  private void pdfKpi(PdfPTable tbl, String val, String lbl, Color color, Font vFont, Font lFont) {
    PdfPCell c = new PdfPCell();
    c.setBackgroundColor(color);
    c.setBorder(Rectangle.NO_BORDER);
    c.setPaddingTop(18);
    c.setPaddingBottom(18);
    c.setPaddingLeft(8);
    c.setPaddingRight(8);
    Paragraph p = new Paragraph();
    p.setAlignment(Element.ALIGN_CENTER);
    p.add(new Chunk(val + "\n", vFont));
    p.add(new Chunk(lbl, lFont));
    c.addElement(p);
    tbl.addCell(c);
  }

  // ==================== EXCEL HELPERS ====================

  private XSSFCellStyle xlStyle(
      XSSFWorkbook wb,
      java.awt.Color bg,
      java.awt.Color fg,
      int size,
      boolean bold,
      boolean center) {
    XSSFCellStyle st = wb.createCellStyle();
    if (bg != null) {
      st.setFillForegroundColor(new XSSFColor(bg, null));
      st.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
    XSSFFont font = wb.createFont();
    font.setBold(bold);
    font.setFontHeightInPoints((short) size);
    if (fg != null) font.setColor(new XSSFColor(fg, null));
    st.setFont(font);
    if (center) st.setAlignment(HorizontalAlignment.CENTER);
    st.setVerticalAlignment(VerticalAlignment.CENTER);
    st.setWrapText(true);
    st.setBorderBottom(BorderStyle.THIN);
    st.setBorderTop(BorderStyle.THIN);
    st.setBorderLeft(BorderStyle.THIN);
    st.setBorderRight(BorderStyle.THIN);
    XSSFColor borderColor = new XSSFColor(new java.awt.Color(200, 210, 220), null);
    st.setBottomBorderColor(borderColor);
    st.setTopBorderColor(borderColor);
    st.setLeftBorderColor(borderColor);
    st.setRightBorderColor(borderColor);
    return st;
  }

  private void xlCell(XSSFRow row, int col, String value, XSSFCellStyle style) {
    XSSFCell cell = row.createCell(col);
    cell.setCellValue(value != null ? value : "");
    cell.setCellStyle(style);
  }

  private int xlSection(XSSFSheet sheet, int rowIdx, String title, XSSFCellStyle style, int cols) {
    XSSFRow row = sheet.createRow(rowIdx);
    row.setHeight((short) 550);
    XSSFCell cell = row.createCell(0);
    cell.setCellValue(title);
    cell.setCellStyle(style);
    sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, cols - 1));
    return rowIdx + 1;
  }

  // ==================== SHARED HELPERS ====================

  private String rptTranslateStatus(SurveyStatus status) {
    if (status == null) return "N/A";
    switch (status) {
      case ACTIVE:
        return "Activa";
      case DRAFT:
        return "Borrador";
      case CLOSED:
        return "Cerrada";
      case PAUSED:
        return "Pausada";
      case COMPLETED:
        return "Completada";
      default:
        return status.name();
    }
  }

  private String rptTranslateType(
      pe.com.graduate.insights.api.features.survey.domain.model.QuestionType type) {
    if (type == null) return "";
    switch (type) {
      case YES_NO:
        return "Sí/No";
      case SCALE:
        return "Escala";
      case TEXT:
        return "Texto";
      case NUMBER:
        return "Número";
      case DATE:
        return "Fecha";
      case EMAIL:
        return "Email";
      case PHONE:
        return "Teléfono";
      case SINGLE_CHOICE:
        return "Opción Única";
      case MULTIPLE_CHOICE:
        return "Opción Múltiple";
      default:
        return type.name();
    }
  }

  private String rptTranslateGender(String gender) {
    if (gender == null) return "N/A";
    switch (gender.toLowerCase()) {
      case "m":
      case "masculino":
      case "male":
        return "Masculino";
      case "f":
      case "femenino":
      case "female":
        return "Femenino";
      case "otro":
      case "other":
        return "Otro";
      default:
        return gender;
    }
  }

  private String rptTranslateMonth(String month) {
    if (month == null) return "";
    switch (month.toUpperCase()) {
      case "JANUARY":
        return "Enero";
      case "FEBRUARY":
        return "Febrero";
      case "MARCH":
        return "Marzo";
      case "APRIL":
        return "Abril";
      case "MAY":
        return "Mayo";
      case "JUNE":
        return "Junio";
      case "JULY":
        return "Julio";
      case "AUGUST":
        return "Agosto";
      case "SEPTEMBER":
        return "Septiembre";
      case "OCTOBER":
        return "Octubre";
      case "NOVEMBER":
        return "Noviembre";
      case "DECEMBER":
        return "Diciembre";
      default:
        return month;
    }
  }

  private String rptGetAnswer(SurveyStatisticsRepositoryPort.QuestionResponseData r) {
    if (r.textResponse() != null && !r.textResponse().isBlank()) return r.textResponse();
    if (r.numericResponse() != null) return r.numericResponse().toString();
    if (r.selectedOptionTexts() != null && !r.selectedOptionTexts().isEmpty())
      return String.join("; ", r.selectedOptionTexts());
    return "";
  }

  private String csvEsc(String v) {
    if (v == null) return "";
    if (v.contains(",") || v.contains("\"") || v.contains("\n"))
      return "\"" + v.replace("\"", "\"\"") + "\"";
    return v;
  }

  private Map<String, Long> getWordFrequency(List<String> textResponses) {
    Map<String, Long> wordCount = new HashMap<>();

    for (String response : textResponses) {
      if (response != null && !response.trim().isEmpty()) {
        String[] words =
            response
                .toLowerCase()
                .replaceAll("[^a-z\u00e1\u00e9\u00ed\u00f3\u00fa\u00f1\u00fc\\s]", "")
                .split("\\s+");

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
                Collectors.toMap(Enum::name, surveyStatisticsRepositoryPort::countSurveysByStatus));

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
        return response.gender() != null ? response.gender() : "No especificado";
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
