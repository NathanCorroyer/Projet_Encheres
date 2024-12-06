function updateDate() {
	const dateElement = document.getElementById("current-date");
	const now = new Date();
	const day = String(now.getDate()).padStart(2, '0'); // Ajoute un zéro devant si nécessaire
	 const month = String(now.getMonth() + 1).padStart(2, '0'); // Mois commence à 0
	 const year = now.getFullYear();

	 // Formater la date en dd/mm/yyyy
	 const formattedDate = `${day}/${month}/${year}`;
	dateElement.textContent = formattedDate;
}
// Mettre à jour la date au chargement de la page
$(document).ready(function(){
	updateDate();
	// Optionnel : Actualiser la date chaque minute
	setInterval(updateDate, 60000);
})
