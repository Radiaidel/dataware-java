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
