# java-kanban
Таск-менеджер с сохранением информации в файле и на http-сервере.
Канбан-доска с возможностью создания и редактирования задач, эпиков и подзадач.

Эпики состоят из подзадач, статус эпика зависит от статусов всех входящих в него подзадач. Каждая задача может иметь статус Новавя, В процессе, Выполненная.

Ведется история обращений к задачам.

Список всех задач и история обращений может хранится в памяти, в файле или на Http-сервере в виде ключ-значение.

Java 11, JUnit, google.Gson

Запуск приложения:
1) Скачать zip-архив
2) Распаковать
3) Открыть проект в IDEA
4) Запустить src/Main

