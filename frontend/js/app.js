const api = "http://localhost:8080";
let data = "";
let res;
let json;
const newToDoList = document.getElementById("newTodoList");
const createTodoList = document.getElementById("btnCreateToDoList")
const form = document.querySelector("form");

const container = document.getElementById("container")
// const template = document.getElementById("containerToDoList").content;
let toDoListCreated = document.getElementById("ToDoListCreated");
let deleteList = document.getElementById("btnDeleteToDoList");
let newTodo = document.getElementById("newTodo");
let createTodo = document.getElementById("btnCreateToDo")
let table = document.getElementById("listToDo")
let idTodo = document.getElementById("id")
let toDo = document.getElementById("toDo")
let completed = document.getElementById("completed")
let editTodo = document.getElementById("edit")
let deleteToDo = document.getElementById("delete")
const fragment = document.createDocumentFragment();
let tbody = document.querySelector("tbody")
let data1 = ""
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
            <td class="id">${toDo.id}</td>
            <td class="Tarea">${toDo.name}</td>
            <td class="completado">
                <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault">
                <label class="form-check-label" for="flexSwitchCheckDefault"></label>
            </td>
            <td class="opciones">
                <button type="button" id="editar${toDo.id}" style="margin-right: 1rem" class="btn btn-primary">Editar</button>
                <button class="eliminar btn btn-danger" type="button" id="eliminar${toDo.id}" >Eliminar</button>
            </td>
        </tr>`
            })
            todoList += ` <hr>
    <div>
        <h2 id="nombre-lista" style="text-align: start">${toDoList.name}</h2>
        <spam class = "spamId" area hidden true>${toDoList.id}</spam>
        <button class="EliminarTarea btn btn-outline-danger" type="submit" id="borrar${toDoList.id}" ">Eliminar</button>
    </div> 
    <br>
    <div class="input-group mb-5">
        <input class="form-control me-sm-2" style="width: 50%" type="text" id="inputTarea${toDoList.id}" placeholder="¿Que piensas hacer?">
        <button class="agregarSubList btn btn-primary my-2 my-sm-0" type="submit" value="${toDoList.id}">Crear</button>
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



