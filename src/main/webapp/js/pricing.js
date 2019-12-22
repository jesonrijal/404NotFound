// Get the modal
var modal = document.getElementById('modal-form');

var onlinePayment = document.getElementById("payOnline");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("modal-close")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  closeModal();
}

function closeModal() {
	modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    closeModal();
  }
} 

function openModal(button) {
	modal.style.display = "block";
	var sel = document.getElementById("ageSelector")
	var opts = sel.options;
	for (var opt, i = 0; opt = opts[i]; i++) {
		if (opt.value == button.value) {
			sel.selectedIndex = i;
		}
	}
}

function openPayment() {
	onlinePayment.style.display = "inline-grid";
}

function closePayment() {
	onlinePayment.style.display = "none";
}

function paymentChange(radio) {
	if (radio.value === "online") {
		openPayment();
	}
	else {
		closePayment();
	}
}

function resetForm() {
	document.getElementById("userInputForm").reset();
}