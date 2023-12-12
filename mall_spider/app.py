from flask import Flask

from spider.views import product_blue
from config.nacos_config import NACOS_CONFIG
from nacos import NacosClient

app = Flask(__name__)


# 商品模块


def register_blue(app):
    app.register_blueprint(product_blue, url_prefix='/product')


def register_nacos():
    client = NacosClient(NACOS_CONFIG.get('SERVER_ADDRESSES'), namespace=NACOS_CONFIG.get('NAMESPACE'),
                         username=NACOS_CONFIG.get('username'), password=NACOS_CONFIG.get('password'))

    client.add_naming_instance(NACOS_CONFIG.get('service_name'), 'localhost', 15000, 'DEFAULT')


register_blue(app)
register_nacos()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=15000, debug=True)
