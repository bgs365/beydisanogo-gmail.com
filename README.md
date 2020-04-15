DATABASE SETTING

    Install mysql
    
    Create database sb_jwt_secu;
            -> create database sb_jwt_secu;
    
    Create my sql sb_jwt_secu;
        -> GRANT ALL PRIVILEGES ON *.* TO 'sb_jwt_secu'@'localhost' IDENTIFIED BY 'Java1234..';
        
    Set application properties according to my sql customUser
    Lunch application, hybernate will create all tables according to model
    And it will also create super customUser via UserCretor class with all grat on swagger
    
MAIL SETTING 

    Fill following values in Application properties
    spring.mail.host
    spring.mail.port
    spring.mail.username
    spring.mail.password
    
LUNCH SETTING 

    mvn clean install
    cd web
    mvn spring-boot:run
