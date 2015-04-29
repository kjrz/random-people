# random-people


Build image:
```
docker build -t random-people .
```


Run container:
```
docker run -p 4567:4567 random-people
```


Test:
```
$ curl localhost:4567/person
Sylwia Pocztowska, Bandurskiego, Warszawa-Ursus

$ curl "localhost:4567/person?street=Magiera&district=Bielany"
Wiesława Śleszyńska, Magiera, Warszawa-Bielany

$ curl localhost:4567/json/person
{"firstName":"Sławomir","lastName":"Brożyna","district":"Praga Południe","street":"Metalowców"}
```
