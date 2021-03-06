# Алгоритм tg-замыкания

Алгоритм построения tg-замыкания графа доступов и
информационных потоков состоит из пяти шагов.

1. Для каждого субъекта выполнить правило create({t, g, r, w}, s, o), при
этом все созданные объекты заносятся в ```objects```, а ребра в ```edgeMap```
2. Инициализировать: ```L``` – список всех ребер графа доступов и 
информационных потоков и ```N``` – множество вершин.
3. Выбрать из списка ```L``` **первое ребро**. Занести **х и у** во
множество ```N```. Удалить ребро это из списка ```L```.
4. Для всех вершин из ```N``` проверить возможность применения
правил **take** или **grant** на тройке вершин **{х, у, z}**, где **x, y**
это вершины с третьего шага, а **z** - вершина из множества ```N```. 
Если в результате применения правил take или grant появляются новые ребра 
с правами **t,g**, занести их в конец списка ```L``` и множество ```Е```.
5. Пока список ```L``` не пуст, перейти на шаг 3.
