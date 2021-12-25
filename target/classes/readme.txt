- 注意：
    - 项目在打包上线的时候，springboot默认打的是jar包，所以需要在pom.xml文件中
    - 设置项目的打包类型为war包


    - springboot是内置tomcat的，所以在上线打包的时候需要排除内置tomcat的影响，
    - 所以需要在dependencies中设置排除掉内置的tomcat容器


    - springboot默认打包的文件名为项目名加上版本号，我们可以重新设置打包的项目的
    - 名字，在<build>中使用<finalName>crm</finalName>进行设置打包好的包名

    - 项目打包上线时需要忽略自带的mysql版本号

    - 由于springboot项目默认是使用main方法去启动整个项目，但是打包上线之后就没有
    - 了main方法，所以需要重新进行设置，让主启动类继承SpringBootServletInitializer,
    - 然后重写父类的configure()方法即可
