import time

from selenium import webdriver


def open_url(url):
    # 实例化浏览器驱动对象（创建浏览器驱动对象）
    driver = webdriver.Firefox()  # 创建的是谷歌浏览器驱动对象   chrome后面有括号，而且第一个字母要大写
    # driver = webdriver.Firefox() # 创建火狐浏览器驱动对象
    # 打开百度网站
    driver.get(url)
    time.sleep(3)  # 通过快捷导包的方式导入time模块， 光标要在time后面再按alt+enter
    # 退出浏览器驱动(释放系统资源)
    driver.quit()
