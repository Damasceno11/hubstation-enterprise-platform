# --- ETAPA 1: BUILD (Node.js) ---
FROM node:22-alpine as build
WORKDIR /app

# Copia os arquivos de dependência e instala
COPY package.json package-lock.json ./
RUN npm install --legacy-peer-deps

# Copia todo o código fonte
COPY . .

# Gera o build de produção (otimizado)
RUN npm run build -- --configuration production

# --- ETAPA 2: SERVIDOR (Nginx) ---
FROM nginx:alpine

# Copia os arquivos compilados do Angular para a pasta do Nginx
# ATENÇÃO: 'posto-combustivel-front' deve ser o nome do projeto no angular.json
# Se der erro, verifique se a pasta dist/ tem outro nome
COPY --from=build /app/dist/posto-combustivel-front/browser /usr/share/nginx/html

# Copia nossa configuração customizada do Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Expõe a porta 80 (interna do container)
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]