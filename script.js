document.getElementById('uploadForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Останавливаем отправку формы стандартным способом

    // Получаем файл из input
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];

    const formData = new FormData();
    formData.append('file', file);

    // Отправляем файл на сервер с помощью fetch API
    fetch('http://localhost:8080/upload', { // Замените URL на ваш адрес бэкенда
        method: 'POST',
        body: formData
    })
        .then(response => response.json()) // Предполагаем, что сервер отвечает JSON
        .then(data => {
            // Обрабатываем успешный ответ от сервера
            document.getElementById('result').innerHTML = "Файл успешно загружен!";
            console.log(data); // Данные от сервера
        })
        .catch(error => {
            // Обрабатываем ошибки
            document.getElementById('result').innerHTML = "Произошла ошибка при загрузке файла.";
            console.error('Ошибка:', error);
        });
});