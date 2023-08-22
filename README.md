# Процедура запуска автотестов:
- Запустить Docker Desktop(или другое подобное приложение)
- В терминале выполнить команду для запуска приложения aqa-shop.jar 
- для взаимодействия с базой данных MySQL
  (java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" "-Dspring.datasource.username=app" "-Dspring.da
  tasource.password=pass" -jar ./artifacts/aqa-shop.jar)
- для взаимодействия с базой данных PostgreSQL
  (java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" "-Dspring.datasource.username=app" "-Dspri
  ng.datasource.password=pass" -jar ./artifacts/aqa-shop.jar)
- В терминале папки gate-simulator выполнить команду для запуска симулятора банковских сервисов(npm start)
- Запустить тесты через терминал командой:
- для MySQL (./gradlew test "-Ddb.url=jdbc:mysql://localhost:3306/app" "-Ddb.username=app" "-Ddb.password=pass")
- для PostgreSQL (./gradlew test "-Ddb.url=jdbc:postgresql://localhost:5432/app" "-Ddb.username=app" "-Ddb.password=pass")


