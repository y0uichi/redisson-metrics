FROM python:3.11-slim

WORKDIR /app

# Install redis-cli
RUN apt-get update && apt-get install -y redis-tools curl && rm -rf /var/lib/apt/lists/*

COPY . /app

RUN pip install --no-cache-dir flask

EXPOSE 80

CMD ["python", "main.py"] 