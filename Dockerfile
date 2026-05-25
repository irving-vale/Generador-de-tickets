# IMAGEN MODELO
FROM eclipse-temurin:21.0.7_6-jdk
# --- AQUÍ INSTALAMOS NODE 22 (LTS) ---
# Usamos el script oficial de NodeSource para Debian (que es la base de temurin)
RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_22.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*
# -------------------------------------
#DEFINIR DIRECTORIO RAIZ
WORKDIR /app

#COPIAR ARCHIVOS DENTRO DEL CONTENEDOR
COPY  ./pom.xml     /app
COPY  ./.mvn    /app/.mvn
COPY  ./mvnw    /app

# DESCARGAR LAS DEPENDENCIAS DE MAVEN
RUN ./mvnw dependency:go-offline -B

# COPIAR EL CODIGO FUENTE DENTRO DEL CONTENEDOR
COPY ./src  /app/src

# CONSTRUIR NUETSRA APLICACION
RUN ./mvnw package -DskipTests -B


