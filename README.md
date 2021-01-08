# Shelter

![image](https://i.imgur.com/sPnJf2m.png)

[![name](https://i.imgur.com/eTZly6n.png)](https://play.google.com/store/apps/details?id=com.ccr.shelter)

## Descripción del proyecto

Shelter es una aplicación destinada a ser utilizada por asociaciones protectoras de animales y refugios.

Permite llevar un control de los animales del refugio o la protectora en una base de datos. Es posible crear un perfil para cada animal, con detalles como el nombre, la edad, la raza, la fecha de nacimiento o el peso.

También se da la posibilidad de introducir valores relacionados con el historial médico del animal, de modo que sea más fácil determinar si este ha sido esterilizado y vacunado.

Una vez el animal haya sido adoptado, será posible añadir una fecha de adopción e introducir otros detalles de interés. Cuando el animal haya sido adoptado, se mostrará un icono en la tarjeta de su perfil en la pantalla de inicio.

****

La aplicación está desarrollada en base al patrón de arquitectura MVVM, de modo que la interfaz de usuario se encuentra desacoplada de la lógica de la aplicación.

Su diseño está elaborado a través de los elementos disponibles en la última versión de Material Components.

## Estructura de la aplicación

### CatalogActivity.java

Es la actividad principal de la aplicación. Contiene un widget SearchView que permite realizar la búsqueda de los animales almacenados en la base de datos.

También se incluye un RecyclerView formado por items descritos en un _layout_, donde se mostrará un resumen de la ficha de los animales almacenados. Al tocar sobre cada uno de ellos, se puede acceder al perfil de cada animal.

En la parte inferior, aparece un botón flotante de acción que permite acceder a la segunda actividad, donde será posible añadir un nuevo animal.

### EditorActivity.java

Es la actividad que permite añadir un nuevo animal a la base de datos, introduciendo los valores necesarios.

Se trata de una actividad multipropósito: es posible añadir un nuevo animal a la base de datos, o bien editar aquellos que ya están presentes.

Dependiendo de la acción que se va a realizar (añadir, eliminar o actualizar) se mostrarán distintos iconos en el widget _toolbar_ de la aplicación, que permiten actualizar, guardar o eliminar una mascota.




