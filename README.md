
# 执行example
在环境变量中配置命名服务器地址，多个用";"隔开

```
# 在同一个环境中打开idea
export NAMESRV_ADDR=192.168.6.166:9876 && idea
# 或者设置为系统变量，idea中执行需要重启
echo 'export NAMESRV_ADDR=192.168.6.166:9876' >> ~/.zshrc && source ~/.zshrc
```


