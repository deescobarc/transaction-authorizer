# Lulo Bank: Code Challenge: Transaction Authorizer

## Instalación

### Requerimientos

Tener instalado docker (versión mínima 20.10.8)

### Construcción de la aplicación

Descargar el repositorio y en una terminal construir el jar y el contenedor con los siguientes comandos:

- ./gradlew build
- docker build -t transactionauthorizerdocker .

### Ejecutar aplicación

Para probar la aplicación se ejecuta el siguiente comando, el cual es encargado de levantar el contenedor y exponer la aplicación

- docker run -p 5000:9090 transactionauthorizerdocker

## Manual de usuario

La aplicación consta de un solo endpoint http://localhost:5000/api/accounts que se consume con verbo POST. Las dos operaciones principales son:

### Creación de cuentas

Para crear una cuenta se debe enviar un json con el siguiente formato:

```
{
    "account": {
        "id": 1,
        "active-card": true,
        "available-limit": 350
    }
}
```

Lo que devuelve la información de la cuenta creada como se observa en la siguiente imagen:

![img.png](img.png)

### Autorización de transacciones

Las transaciones se ejecutan sobre el mismo endpoint, enviando un json con el siguiente formato:

```
{
    "transaction": {
        "id": 1,
        "merchant": "Habbib's",
        "amount": 90,
        "time": "2019-02-13T11:00:00.000Z"
    }
}
```

Al ejecutar la transacción se devuelve la información de la cuenta actualizada con la nueva cantidad disponible como se puede observar en la siguiente imagen, además de un listado de violaciones en caso de no cumplir alguna regla de negocio:

![img_1.png](img_1.png)

## Manual técnico

- Se implementó una solución basada en microservicios
- Se utilizó arquitectura hexagonal basada en los módulos de: Infraestructura, Puertos, Adaptadores, Aplicación y Dominio
- Lenguaje de programación JAVA 1.8
- Framework Spring Boot
- Contenedor Docker
- CQRS basado en un bus de comandos y consultas
- Algunos de los patrones utilizados fueron: Entity, repository, domain events y builder.  
- Se implementaron técnicas de clean code
- Parte del desarrollo fue con la técnica TDD
- Se realizaron test unitarios y de integración


# Acerca del desarrollador

- David Esteban Escobar
- Ingeniero de Sistemas e Informática de la Universidad Nacional de Colombia
- 3 Años de experiencia en desarrollo de software