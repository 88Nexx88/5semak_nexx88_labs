# De facto rules
Класс, в котором описаны правила де факто.
Пунктир - это мнимые ребра, потоки (fr, fw), а обычные - реальные ребра (t, g, r, w). 
Закрашенные вершины - субъекты, незакрашенные - объекты

* **Pass**
> ![alt](http://nob.cs.ucdavis.edu/classes/ecs235b-2013-02/handouts/tg-pass.jpg)
> 
> Если субъект имеет право чтения и записи к разным объектам, то между ними возникает поток на чтение.
* **Spy**
> ![alt](http://nob.cs.ucdavis.edu/classes/ecs235b-2013-02/handouts/tg-spy.jpg)
> 
> А тут один субъект имеет право на чтение другого субъекта, 
> соответственно имеет так же право на чтение всех его объектов (которые он сам может читать). 
* **Post**
> ![alt](http://nob.cs.ucdavis.edu/classes/ecs235b-2013-02/handouts/tg-post.jpg)
> 
> Если у какого-либо субъекта есть право на чтение к объекту, а так же к этому 
> объекту имеет право на запись другой субъект, то первый субъект может читать второй.
* **Find**
![alt](http://nob.cs.ucdavis.edu/classes/ecs235b-2013-02/handouts/tg-find.jpg)
> Если субъект имеет право записи к другому субъекту, у которо есть такое же право записи к какому-либо объекту, то тогда возникает поток на чтение.
* **Первое правило - First**
> 
> Если у субъекта-1 есть право чтения к объекту-2, то от второго к первому появляется поток на запись (fw).
* **Второе правило - Second**
> 
> Если у субъекта-1 есть право записи к объекту-2, то от второго к первому появляется поток на чтение (fr).
Еще немного примеров для понимания [здесь](http://nob.cs.ucdavis.edu/classes/ecs235b-2013-02/handouts/tg-stuff.html).