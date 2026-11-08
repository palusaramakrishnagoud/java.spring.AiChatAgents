#!/bin/bash

# API URL
# https://huggingface.co - Find models provided by the shared API - use reference_provider=hf-inference

PROMPT="summarize: In today's Q3 planning meeting, the team discussed improving both technical reliability and user experience for our mobile payment solution."

# brew install jq
# Using jq to construct the JSON payload safely. jq is a lightweight command-line tool for processing, filtering, and manipulating JSON data. It allows you to construct

# Final payload
JSON_PAYLOAD=$(jq -n --arg inputs "$PROMPT" '{
    inputs: $inputs,
    parameters: { max_new_tokens: 20, temperature: 0.7 },
    options: { wait_for_model: true }
}')

echo "Request payload:"
echo "$JSON_PAYLOAD" | jq .

# https://huggingface.co
MODEL="facebook/bart-large-cnn"

# Call the open Inference API which is used to play with multiple models
curl https://router.huggingface.co/hf-inference/models/$MODEL \
    -X POST \
    -H "Authorization: Bearer $HUGGINGFACE_API_KEY" \
    -H "Content-Type: application/json" \
    -d "$JSON_PAYLOAD"