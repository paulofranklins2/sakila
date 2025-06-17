// DOM references and pagination setup
const actorTableBody = document.querySelector('#actorTable tbody');
const currentPageDisplay = document.getElementById('currentPageDisplay');
const prevBtn = document.getElementById('prevPageBtn');
const nextBtn = document.getElementById('nextPageBtn');
let deleteActorId = null;
let currentPage = 0;
const pageSize = 20; // Number of actors per page

// Load and display actors for a given page
function fetchActors(page) {
    fetch(`http://localhost:8080/actor/page/${page}`)
        .then(res => res.json())
        .then(data => {
            actorTableBody.innerHTML = '';

            // If no actors returned and not on first page, disable next and stop
            if (data.length === 0 && page > 1) {
                nextBtn.disabled = true;
                return;
            }

            data.forEach((actor, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
<!--                  <td>${(page - 1) * pageSize + index + 1}</td>-->
                  <td>${actor.actorId}</td>
                  <td>${actor.firstName}</td>
                  <td>${actor.lastName}</td>
                  <td>
                    <button class="btn btn-sm btn-warning me-1" onclick="openUpdateModal(${actor.actorId}, '${actor.firstName}', '${actor.lastName}')">Update</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteActor(${actor.actorId}, '${actor.firstName} ${actor.lastName}')">Delete</button>
                  </td>
                `;
                actorTableBody.appendChild(row);
            });

            // Update current page UI
            currentPage = page;
            currentPageDisplay.textContent = `Page ${currentPage}`;
            prevBtn.disabled = currentPage === 0;

            // Disable next if fewer than 20 items (no full page means no next)
            nextBtn.disabled = data.length < pageSize;
        })
        .catch(err => {
            actorTableBody.innerHTML = '<tr><td colspan="5">Error loading data</td></tr>';
            console.error(err);
        });
}

// Pagination navigation
prevBtn.addEventListener('click', () => {
    if (currentPage > 0) fetchActors(currentPage - 1);
});

nextBtn.addEventListener('click', () => fetchActors(currentPage + 1));

// Open update modal and populate with current data
function openUpdateModal(id, firstName, lastName) {
    document.getElementById('updateActorId').value = id;
    document.getElementById('firstName').value = firstName;
    document.getElementById('lastName').value = lastName;
    new bootstrap.Modal(document.getElementById('updateModal')).show();
}

// Set actor to delete and show delete confirmation modal
function deleteActor(id, fullName) {
    deleteActorId = id;
    document.getElementById('deleteModalBody').textContent = `Are you sure you want to delete actor "${fullName}"?`;
    new bootstrap.Modal(document.getElementById('deleteModal')).show();
}

// Confirm delete action
const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
confirmDeleteBtn.addEventListener('click', () => {
    if (deleteActorId == null) return;
    fetch(`http://localhost:8080/actor/delete/${deleteActorId}`, { method: 'DELETE' })
        .then(res => {
            if (res.ok) {
                bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();
                fetchActors(currentPage);
            } else {
                alert('Failed to delete actor');
            }
        });
});

// Submit update actor form
const updateForm = document.getElementById('updateForm');
updateForm.addEventListener('submit', e => {
    e.preventDefault();
    const id = document.getElementById('updateActorId').value;
    const firstName = document.getElementById('firstName').value.trim();
    const lastName = document.getElementById('lastName').value.trim();

    fetch(`http://localhost:8080/actor/update/${id}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ actorId: id, firstName, lastName })
    })
        .then(res => {
            if (res.ok) {
                bootstrap.Modal.getInstance(document.getElementById('updateModal')).hide();
                fetchActors(currentPage);
            } else {
                alert('Failed to update actor');
            }
        });
});

// Submit add actor form
const addForm = document.getElementById('addForm');
addForm.addEventListener('submit', e => {
    e.preventDefault();
    const firstName = document.getElementById('addFirstName').value.trim();
    const lastName = document.getElementById('addLastName').value.trim();

    fetch(`http://localhost:8080/actor/save`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ firstName, lastName })
    })
        .then(res => {
            if (res.ok) {
                bootstrap.Modal.getInstance(document.getElementById('addModal')).hide();
                addForm.reset();
                fetchActors(currentPage);
            } else {
                alert('Failed to add actor');
            }
        });
});

// Load first page of actors when page loads
fetchActors(currentPage);