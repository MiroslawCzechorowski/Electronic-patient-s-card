![header](http://imagizer.imageshack.us/a/img907/8701/vWkuKF.jpg)

## Członkowie zespołu :
Kacper Mazurkiewicz, 110235. email: kacper267@op.pl  (aplikacja iOS)

Mirosław Czechorowski, 113975. email: miro.cz92@gmail.com (aplikacja Android)

# Opis projektu
"Elektroniczna karta pacjenta" składa się z bazy danych przechowującej informacje na temat pacjentów takie jak: historia choroby, zdjęcia rentegnowskie, dane osobowe, czy opis pobytu w szpitalu. Do bazy danych łączymy się za pomocą klienta mobilnego, który umożliwia przeglądanie oraz edycję w/w danych. Aplikacja usprawnia prowadzenie dokumentacji lekarskiej, która jest dostępna z każdego miejsca. Dzięki temu rozwiązaniu osoby mające dostęp do bazy danych mogą usprawnić przeprowadzanie wywiadu z poszkodowanym. 

# Założona funkcjonalność

Po stronie serwera:
- [x] Obsługa żądań HTTP

Po stronie klienta:
- [x] Logowanie do aplikacji
- [x] Wyświetlanie listy pacjentów
- [x] Edycja danych
- [x] Dodawanie pacjentów
- [x] Wysyłanie zdjęć do aplikacji


# Opis architektury

Aplikacja mobilna pobiera dane z bazy MySQL za pośrednictwem API. 

# Środowisko realizacji projektu

Po stronie serwera:
- PHP

Po stronie klienta:
- Objective-C (programowanie aplikacji iOS)
- SBJson (Biblioteka służąca do parsowania formatu Json)
- Java for Android (programowanie aplikacji Android)
- org.json (Biblioteka służąca do parsowania formatu Json)