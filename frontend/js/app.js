const api = "http://localhost:8080";
const $form = document.getElementById("formList");
const container = document.getElementById("container")
let res;
let json;
let todo = ""
let todoList = ""
let todos= {}

/**
 * Funcion para obtener la informacion de la base de datos
 * @returns {Promise<void>}
 */
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
                <input class="validate form-check-input" id="validate${toDo.id}" type="checkbox" id="flexSwitchCheckDefault">
                <label class="form-check-label" for="flexSwitchCheckDefault"></label>
            </td>
            <td>
                <button class="edit btn btn-primary" value="${toDo.id}" type="button" id="edit${toDo.id}" style="margin-right: 1rem" >Editar</button>
                <button class="deleteToDo btn btn-danger" type="button" id="delete${toDo.id}" >Eliminar</button>
            </td>
        </tr>`
            })
            todoList += ` <hr>
 <div id="${toDoList.id}">
    <div id="${toDoList.id}">
        <h2 id="nombre-lista" style="text-align: start">${toDoList.name}</h2>
        <button class="deleteToDoList btn btn-outline-danger" type="submit" id="delete${toDoList.id}" ">Eliminar</button>
    </div> 
    <br>
    <div class="input-group mb-5">
        <input class="form-control me-sm-2" style="width: 50%" type="text" id="newToDoList${toDoList.id}" placeholder="¿Que piensas hacer?">
        <button class="saveToDo btn btn-primary my-2 my-sm-0" type="submit" id="create${toDoList.id}" value="${toDoList.id}">Crear</button>
        <button style="display:none;" class="editTodo btn btn-secondary my-2 my-sm-0" type="submit" id="update${toDoList.id}" value="${toDoList.id}">Actualizar</button
    </div>
    <table id="${toDoList.id}" class="table mt-2 table-bordered" style="text-align: center">
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
    </div>
        `
        })
        container.innerHTML = todoList;
        todoList = "";
    } catch (err) {
        let message = err.statusText || "Ocurrió un error";
        container.insertAdjacentHTML("afterend", `<p style="text-align: center"><b>Error ${err.status}: ${message}</b></p>`);
    }

}

/**
 * Evento que escucha la funcion para obtener la informacion de la base de datos cuando el DOM se cargue
 */
document.addEventListener("DOMContentLoaded", getData());

/**
 * Evento que escucha el envio del formulario para crear nueva lista e invoca la funcion
 */
$form.addEventListener("submit", e => {
    e.preventDefault()
    createList(document.getElementById("newTodoList").value)
})


/**
 * Funcion para crear una toDoList
 * @param newList Nombre de la toDoList
 * @returns {Promise<void>}
 */
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

/**
 * Evento que escucha el click en el contenenedor y evalua el nombre de la clase donde se dio click
 * Se invocan las funciones determinadas para el click
 */
container.addEventListener("click", (e) => {
    if (e.target.classList[0] === "deleteToDoList") {
        deleteList(e.target.parentElement.parentElement.id)
    }
    if (e.target.classList[0] === "saveToDo") {
        e.preventDefault()
        let data = {
            name: e.target.previousElementSibling.value,
            id: e.path[0].value
        }
        createToDo(data)
    }
    if (e.target.classList[0] === "editTodo") {
        let input = e.path[1].children[0].value;
        editToDo(todos.idpadre, todos.id, input)
    }
    if (e.target.classList[0] === "deleteToDo") {
        deleteToDo(e.target.parentElement.parentElement.children[0].textContent)
    }
    if (e.target.classList[0] === "edit") {
        e.preventDefault()
        todos.id = e.path[0].value
        todos.name = e.path[2].children[1].textContent;
        todos.idpadre = e.path[4].id;
        let input = e.path[5].children[0];
        let btncrear = document.getElementById('create' + e.path[4].id)
        let boton = document.getElementById('update' + e.path[4].id)
        btncrear.style.display = "none";
        boton.style.display = "";
        input.value = todos.name
    }
    if (e.target.classList[0] === "validate") {
        console.log(e.path[2].children[3].children[0].value);
        let btnvalidar = document.getElementById('edit' + e.path[2].children[3].children[0].value)
        let check = document.getElementById('validate' + e.path[2].children[3].children[0].value).checked
        if (check) {
            btnvalidar.disabled = true;
        } else {
            btnvalidar.disabled = false;
        }
    }
})

/**
 * Funcion para eliminar una toDoList
 * @param id Id del toDo
 * @returns {Promise<void>}
 */
async function deleteList(id) {
    res = await fetch(`${api}/todo-list/${id}`, {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json'
        },
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

/**
 * Funcion para crear un toDo
 * @param name Nombre del toDo
 * @param id Id del toDoList
 * @returns {Promise<void>}
 */
async function createToDo({name, id}) {
    res = await fetch(`${api}/todo`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: name,
            completed: false,
            todoListId: id
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

/**
 * Funcion para editar un toDo
 * @param id Identificador del toDo
 * @returns {Promise<void>}
 */
async function editToDo(id1, id2, nombre) {
    console.log(nombre)
        res = await fetch(`${api}/todo/${id2}`, {
            method: "PUT",
            headers: {
                "Content-type": "application/json; charset=utf-8"
            },
            body: JSON.stringify({
                name: nombre,
                completed: false,
                todoListId: id1
            })
        })
            .then(response => response.json())
            .then(() => {
                console.log(res)
                location.reload()
                getData()
            })
            .catch(err => {
                let message = err.statusText || "Ocurrió un error";
                container.insertAdjacentHTML("afterend", `<p style="text-align: center"><b>Error ${err.status}: ${message}</b></p>`);
            })
}

/**
 * Funcion para eliminar un toDo
 * @param id Identificador del toDo
 * @returns {Promise<void>}
 */
async function deleteToDo(id) {
    res = await fetch(`${api}/todo/${id}`, {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json'
        },
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