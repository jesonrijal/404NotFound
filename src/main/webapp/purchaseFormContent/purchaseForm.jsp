	  
	  <div class="modal-content">
      <div id="formHeader">
        <span class="formTitle">Purchase</span>
	      <span class="modal-close">&times;</span><br>
      </div>
	    <form id="userInputForm" action="pricing" method="post">
        <div class="formName">
          <label for="name">Name:</label><br>
          <input name="firstName" type="text" value="" placeholder="first">
          <input name="lastName" type="text" value="" placeholder="last">
        </div>
        <div class="age">
          <label for="ageGroup">Age:</label>
          <select name="ageGroup" id="ageSelector">
          	<c:forEach items="${ageList}" var="ageGroup">
          		<option value="${ageGroup.getAgeId() }">${ageGroup.getName() }</option>
          	</c:forEach>
          </select>
        </div>
        <div class="payment">
          <input type="radio" name="paymentRadio" value="in-person" id="inPersonRadio" checked="checked" onclick="paymentChange(this)">
          <label for="inPersonRadio">Pay In-Person</label><br>
          <input type="radio" name="paymentRadio" value="online" id="onlineRadio" onclick="paymentChange(this)">
          <label for="onlineRadio">Pay Online</label>
        </div>
        <div id="payOnline">
          <div class="payeeName">
            <label for="payeeName">Name On Card:</label>
            <input name="payeeName" value="" placeholder="full name" type="text">
          </div>
          <div class="ccNumber">
            <label for="cardNumber">Card Number:</label>
            <input name="cardNumber" type="text" value="" placeholder="1234-5678-1234-5678">
          </div>
          <div class="expDate">
            <label for="expDate">Exp Date:</label>
            <input name="expDate" type="text" value="" placeholder="MM/YY">
          </div>
          <div class="CCV">
            <label for="CCV">CCV:</label>
            <input name="CCV" value="" type="text" value="" placeholder="123">
          </div>
        </div>
        <div class="bottomButtons">
	    	<input type="button" value="Cancel" name="cancelButton" onclick="closeModal()">
          	<input type="submit" value="Submit" name="submitForm">
        </div>
	    </form>
	  </div>