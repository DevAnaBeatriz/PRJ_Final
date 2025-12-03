#!/usr/bin/env bash
set -e

MODE="${1:-$CMD}"

if [ "$1" = "lambda" ] || [ "$MODE" = "lambda" ]; then
  echo "Executando em modo: lambda (invoca lambda_handler)"
  python -c "from app import lambda_function as lf; import json; print('Invocando lambda_handler...'); res = lf.lambda_handler({'test': True}, None); print('Lambda retornou:', res)"
else
  echo "Executando em modo: consumer (loop contínuo)"
  python -m app.consumer
fi
