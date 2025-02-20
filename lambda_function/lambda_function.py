import boto3
import json
import os

dynamodb = boto3.resource('dynamodb', endpoint_url='http://localhost:4566')
s3 = boto3.client('s3', endpoint_url='http://localhost:4566')

SQS_QUEUE_URL = "https://localhost.localstack.cloud:4566/000000000000/minha-fila"
DLQ_QUEUE_URL = "https://localhost.localstack.cloud:4566/000000000000/minha-fila-dlq"

def lambda_handler(event, context):
    for record in event['Records']:
        try:
            owner_id = record['body']

            # Busca produtos e categorias no DynamoDB
            products_table = dynamodb.Table('Products')
            categories_table = dynamodb.Table('Categories')

            products = products_table.query(KeyConditionExpression=boto3.dynamodb.conditions.Key('owner_id').eq(owner_id))['Items']
            categories = categories_table.query(KeyConditionExpression=boto3.dynamodb.conditions.Key('owner_id').eq(owner_id))['Items']

            catalog = []
            for category in categories:
                items = [p for p in products if p['category'] == category['title']]
                catalog.append({
                    "category_title": category['title'],
                    "category_description": category['description'],
                    "items": [{"title": p['title'], "price": p['price']} for p in items]
                })

            # Cria bucket e salva arquivo JSON
            bucket_name = f"owner-{owner_id}-catalog"
            try:
                s3.create_bucket(Bucket=bucket_name)
            except s3.exceptions.BucketAlreadyOwnedByYou:
                pass

            s3.put_object(
                Bucket=bucket_name,
                Key='catalog.json',
                Body=json.dumps({"owner_id": owner_id, "catalog": catalog}),
                ContentType='application/json'
            )

        except Exception as e:
            print(f"Error processing message: {e}")
            boto3.client('sqs', endpoint_url='http://localhost:4566').send_message(
                QueueUrl=DLQ_QUEUE_URL,
                MessageBody=record['body']
            )