Ejercicio de Java
El objetivo de este ejercicio es conocer cómo los candidatos a entrar a
almundo.com usan herramientas básicas de Java y diseñan soluciones
orientadas a objetos.
Forma de entrega
La solución tiene que estar pusheada en un repo git. La url del mismo
tiene que ser enviada por email. Como alternativa a git, la solución
puede ser adjuntada (.tar, .zip, etc).
Consigna
Existe un call center donde hay 3 tipos de empleados: operador,
supervisor y director. El proceso de la atención de una llamada
telefónica en primera instancia debe ser atendida por un operador, si
no hay ninguno libre debe ser atendida por un supervisor, y de no
haber tampoco supervisores libres debe ser atendida por un director.
Requerimientos
Diseñar el modelado de clases y diagramas UML necesarios
para documentar y comunicar el diseño.
Debe existir una clase Dispatcher encargada de manejar lasllamadas, y debe contener el método dispatchCall para que las
asigne a los empleados disponibles.
La clase Dispatcher debe tener la capacidad de poder procesar
10 llamadas al mismo tiempo (de modo concurrente).
Cada llamada puede durar un tiempo aleatorio entre 5 y 10
segundos.
Debe tener un test unitario donde lleguen 10 llamadas.
Extras/Plus
Dar alguna solución sobre qué pasa con una llamada cuando no
hay ningún empleado libre.
Dar alguna solución sobre qué pasa con una llamada cuando
entran más de 10 llamadas concurrentes.
Agregar los tests unitarios que se crean convenientes.
Agregar documentación de código.
Tener en Cuenta
El proyecto debe ser creado con Maven.
De ser necesario, anexar un documento con la explicación del
cómo y porqué resolvió los puntos extras, o comentarlo en las
clases donde se encuentran sus tests unitarios respectivos.
MUCHA SUERTE!
