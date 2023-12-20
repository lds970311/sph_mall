from flask import Flask

from spider.models import db
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


app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:19970311@hadoop133:3307/users'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db.init_app(app)
with app.app_context():
    db.create_all()

register_blue(app)
register_nacos()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=15000, debug=True)
