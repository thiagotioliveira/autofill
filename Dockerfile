FROM eclipse-temurin:21-jdk

# Instalar dependências nativas do Tesseract + idioma português
RUN apt-get update && \
    apt-get install -y \
    tesseract-ocr \
    libtesseract-dev \
    libleptonica-dev \
    tesseract-ocr-por && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Definir diretório de trabalho
WORKDIR /app

# Copiar aplicação e dados de idioma (caso você use customizados)
COPY target/*.jar app.jar
COPY tessdata /app/tessdata

# Definir variável de ambiente para o tessdata
ENV TESSDATA_PREFIX=/app/tessdata

# Expor a porta da aplicação
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
