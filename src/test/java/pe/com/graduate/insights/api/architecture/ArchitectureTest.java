package pe.com.graduate.insights.api.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

  private static final String INFRASTRUCTURE_CONTROLLER_PACKAGE = "..infrastructure.controller..";
  private static final String SHARED_REPOSITORY_INFRA_PACKAGE =
      "pe.com.graduate.insights.api.shared.infrastructure.repository..";
    private static final String SHARED_INFRASTRUCTURE_REPOSITORY_ADAPTER_PACKAGE =
      "..infrastructure.repository.adapter..";
    private static final String FEATURE_INFRASTRUCTURE_ADAPTER_PACKAGE =
      "..features..infrastructure.adapter..";

  private static JavaClasses importedClasses;

  @BeforeAll
  static void loadClasses() {
    importedClasses =
        new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("pe.com.graduate.insights.api");
  }

  @Test
  void domainMustNotDependOnInfrastructure() {
    freeze(
        noClasses()
          .that()
          .resideInAnyPackage("..domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..infrastructure.."))
      .check(importedClasses);
  }

  @Test
  void portsMustNotDependOnControllersOrRepositoryAdapters() {
    freeze(
        noClasses()
          .that()
          .resideInAnyPackage("..application.ports..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              INFRASTRUCTURE_CONTROLLER_PACKAGE,
              "..features..infrastructure.controller..",
                SHARED_INFRASTRUCTURE_REPOSITORY_ADAPTER_PACKAGE,
                FEATURE_INFRASTRUCTURE_ADAPTER_PACKAGE,
              "..features..infrastructure.repository.adapter.."))
      .check(importedClasses);
  }

  @Test
  void controllersMustNotAccessJpaRepositoriesDirectly() {
    freeze(
        noClasses()
          .that()
          .resideInAnyPackage(INFRASTRUCTURE_CONTROLLER_PACKAGE)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("..infrastructure.repository.jpa.."))
      .check(importedClasses);
  }

  @Test
  void repositoryAdaptersMustNotDependOnControllers() {
    freeze(
        noClasses()
          .that()
          .resideInAnyPackage(
              SHARED_INFRASTRUCTURE_REPOSITORY_ADAPTER_PACKAGE,
              FEATURE_INFRASTRUCTURE_ADAPTER_PACKAGE)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(INFRASTRUCTURE_CONTROLLER_PACKAGE))
      .check(importedClasses);
  }

  @Test
  void applicationMustNotDependOnSharedRepositoryInfrastructure() {
    freeze(
            noClasses()
                .that()
                .resideInAnyPackage("..features..application..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(SHARED_REPOSITORY_INFRA_PACKAGE))
        .check(importedClasses);
  }

  @Test
  void repositoryAdaptersMustNotDependOnOtherRepositoryAdapters() {
    freeze(
            noClasses()
                .that()
              .resideInAnyPackage(
                SHARED_INFRASTRUCTURE_REPOSITORY_ADAPTER_PACKAGE,
                FEATURE_INFRASTRUCTURE_ADAPTER_PACKAGE)
                .should()
                .dependOnClassesThat()
              .resideInAnyPackage(
                SHARED_INFRASTRUCTURE_REPOSITORY_ADAPTER_PACKAGE,
                FEATURE_INFRASTRUCTURE_ADAPTER_PACKAGE))
        .check(importedClasses);
  }

  @Test
  void domainMustNotContainJpaEntities() {
    freeze(
            classes()
                .that()
                .resideInAnyPackage("..domain..")
                .should()
                .notBeAnnotatedWith(Entity.class))
        .check(importedClasses);
  }

  @Test
  void legacyApplicationPackagesMustNotExist() {
    freeze(
            noClasses()
                .should()
                .resideInAnyPackage(
                    "..application.service..",
                    "..application.ports.input..",
                    "..application.ports.output.."))
        .check(importedClasses);
  }

  @Test
  void rootInfrastructureControllerPackageMustNotExist() {
    freeze(
            noClasses()
                .should()
                .resideInAnyPackage("pe.com.graduate.insights.api.shared.infrastructure.controller.."))
        .check(importedClasses);
  }

  @Test
  void migratedFeatureExceptionsMustNotLiveInGlobalDomainExceptionPackage() {
    freeze(
            noClasses()
                .that()
              .haveNameMatching(
                ".*\\.(AccountPendingApprovalException|DirectorException|EducationCenterException|"
                  + "EventException|EventTypesException|FileException|GraduateException|"
                  + "InvalidCodeException|InvalidCredentialsException|JobException|"
                  + "MailException|SurveyException|SurveyTypeException)$")
                .should()
                .resideInAnyPackage("pe.com.graduate.insights.api.shared.exception.."))
        .check(importedClasses);
  }

  private static ArchRule freeze(ArchRule rule) {
    return FreezingArchRule.freeze(rule);
  }
}
