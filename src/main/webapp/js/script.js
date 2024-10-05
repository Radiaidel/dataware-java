document.addEventListener('DOMContentLoaded', function () {
    // Add an event listener to the modal for when it's shown
    var editTeamModal = document.getElementById('staticBackdrop');
    
    editTeamModal.addEventListener('show.bs.modal', function (event) {
        // Get the button that triggered the modal
        var button = event.relatedTarget; 
        // Extract the team ID from the data-* attributes
        var teamId = button.getAttribute('data-id'); 
        // Update the modal's hidden input field with the team ID
        var editTeamIdInput = document.getElementById('editTeamId');
        editTeamIdInput.value = teamId;
        
        var teamName = button.getAttribute('data-name'); 
        var editNameInput = document.getElementById('editName');
        editNameInput.value = teamName; 
		console.log(teamName, teamId);
    });
});

document.addEventListener('DOMContentLoaded', function () {
    var editModal = document.getElementById('editModal');
    editModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget;
        var id = button.getAttribute('data-id');
        var firstName = button.getAttribute('data-firstname');
        var lastName = button.getAttribute('data-lastname');
        var email = button.getAttribute('data-email');
        var role = button.getAttribute('data-role');

        var modalId = editModal.querySelector('#editMemberId');
        var modalFirstName = editModal.querySelector('#editFirstName');
        var modalLastName = editModal.querySelector('#editLastName');
        var modalEmail = editModal.querySelector('#editEmail');
        var modalRole = editModal.querySelector('#editRole');

        modalId.value = id;
        modalFirstName.value = firstName;
        modalLastName.value = lastName;
        modalEmail.value = email;
        modalRole.value = role;
    });
});
