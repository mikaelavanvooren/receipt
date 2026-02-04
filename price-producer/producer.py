import json
import time
import random
from datetime import datetime
from kafka import KafkaProducer
from faker import Faker

fake = Faker()

KAFKA_BOOTSTRAP_SERVERS = 'localhost:9092'
TOPIC = 'price-updates'
EVENTS_PER_SECOND = 0.5

STORES = ['Walmart', 'Target', 'Whole Foods', 'Aldi', 'Ranch 99']

STORES = ['Walmart', 'Target', 'Kroger', 'Whole Foods', 'Aldi', 'Costco']

PRODUCTS = [
    {'name': 'Milk 1 Gallon',       'category': 'Dairy'},
    {'name': 'Eggs 12ct',           'category': 'Dairy'},
    {'name': 'Bread Whole Wheat',   'category': 'Bakery'},
    {'name': 'Chicken Breast 1lb',  'category': 'Meat'},
    {'name': 'Bananas 1lb',         'category': 'Produce'},
    {'name': 'Orange Juice 52oz',   'category': 'Beverages'},
    {'name': 'Cheddar Cheese 8oz',  'category': 'Dairy'},
    {'name': 'Pasta Penne 16oz',    'category': 'Dry Goods'},
    {'name': 'Olive Oil 16oz',      'category': 'Condiments'},
    {'name': 'Greek Yogurt 32oz',   'category': 'Dairy'},
]

# Realistic price ranges per product
PRICE_RANGES = {
    'Milk 1 Gallon':      (2.99, 5.99),
    'Eggs 12ct':          (2.49, 6.99),
    'Bread Whole Wheat':  (2.99, 5.49),
    'Chicken Breast 1lb': (3.99, 8.99),
    'Bananas 1lb':        (0.29, 0.79),
    'Orange Juice 52oz':  (3.99, 7.99),
    'Cheddar Cheese 8oz': (2.99, 5.99),
    'Pasta Penne 16oz':   (0.99, 2.99),
    'Olive Oil 16oz':     (5.99, 12.99),
    'Greek Yogurt 32oz':  (4.99, 8.99),
}

def create_producer():
    return KafkaProducer(
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        value_serializer=lambda v: json.dumps(v).encode('utf-8'),
        acks='all'
    )

def generate_price_event():
    store = random.choice(STORES)
    product = random.choice(PRODUCTS)
    price_range = PRICE_RANGES[product['name']]
    min_price, max_price = price_range
    price = round(random.uniform(min_price, max_price), 2)
    
    return {
        'store': store,
        'product': {
            'name': product['name'],
            'category': product['category'],
        },
        'price': price,
        'timestamp': datetime.utcnow().isoformat()
    }

def on_send_success(record_metadata):
    print(f"Message sent to {record_metadata.topic} partition {record_metadata.partition} offset {record_metadata.offset}") 

def on_send_error(excp):
    print(f"Failed to send message: {excp}")

## Implemnt next
def main():
    print(f"Starting price producer... topic: {TOPIC} on {KAFKA_BOOTSTRAP_SERVERS}")
    print("Press Ctrl+C to stop.")

    producer = create_producer()
    event_count = 0

    try:
        while True:
            event = generate_price_event()
            producer.send(TOPIC, event).add_callback(on_send_success).add_errback(on_send_error)
            time.sleep(1 / EVENTS_PER_SECOND)
            event_count += 1
            print(f"[Event #{event_count}] {event['store']} - {event['product']['name']}: ${event['price']} at {event['timestamp']}")

            time.sleep(1 / EVENTS_PER_SECOND)

    except KeyboardInterrupt:
        print(f"Stopping price producer. Total events sent: {event_count}")
    finally:
        producer.flush()
        producer.close()

if __name__ == "__main__":
    main()