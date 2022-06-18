const api = "http://localhost:8080";
let res;
let json;
const $form = document.getElementById("formList");

const container = document.getElementById("container")
let todo = ""
let todoList = ""

async function getData() {
    try {
        res = await fetch(`${api}/todo-list`)
        json = await res.json()
        if (!res.ok) throw new Error({status: res.status, statusText: res.statusText});

        json.data.forEach(toDoList => {
            todo = ''
            toDoList.todos.forEach(toDo => {
                todo += ` <tr>
            <td>${toDo.id}</td>
            <td>${toDo.name}</td>
            <td>
                <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault">
                <label class="form-check-label" for="flexSwitchCheckDefault"></label>
            </td>
            <td>
                <button class="editToDo btn btn-primary" type="button" id="edit${toDo.id}" style="margin-right: 1rem" >Editar</button>
                <button class="deleteToDo btn btn-danger" type="button" id="delete${toDo.id}" >Eliminar</button>
            </td>
        </tr>`
            })
            todoList += ` <hr>
    <div>
        <h2 id="nombre-lista" style="text-align: start">${toDoList.name}</h2>
        <spam class = "spamId" area hidden true>${toDoList.id}</spam>
        <button class="deleteToDoList btn btn-outline-danger" type="submit" id="delete${toDoList.id}" ">Eliminar</button>
    </div> 
    <br>
    <div class="input-group mb-5">
        <input class="form-control me-sm-2" style="width: 50%" type="text" id="inputToDo${toDoList.id}" placeholder="¿Que piensas hacer?">
        <button class="saveToDo btn btn-primary my-2 my-sm-0" type="submit" value="${toDoList.id}">Crear</button>
    </div>
    <table id="tabla" class="table mt-2 table-bordered" style="text-align: center">
        <thead>
        <tr>
            <th>ID</th>
            <th>Tarea</th>
            <th>¿Completado?</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
            ${todo}
        </tbody>
    </table>
        `
        })
        container.innerHTML = todoList;
        todoList = "";
    } catch (err) {
        let message = err.statusText || "Ocurrió un error";
        container.insertAdjacentHTML("afterend", `<p style="text-align: center"><b>Error ${err.status}: ${message}</b></p>`);
    }

}

document.addEventListener("DOMContentLoaded", getData());

//Evento que escucha el click de crear nueva lista e invoca la funcion
$form.addEventListener("submit", e => {
    e.preventDefault()
    createList(document.getElementById("newTodoList").value)
})

//Funcion para crear nueva lista
async function createList(newList) {
    res = await fetch(`${api}/todo-list`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: newList,
        })
    })
        .then(response => response.json())
        .then(() => {
            location.reload()
            getData()
        })
        .catch(err => {
            let message = err.statusText || "Ocurrió un error";
            container.insertAdjacentHTML("afterend", `<p style="text-align: center"><b>Error ${err.status}: ${message}</b></p>`);
        })
}

