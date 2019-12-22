<%@page language="java" contentType="text/html" pageEncoding="UTF-8" import="java.sql.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE HTML>
<!--
	Aesthetic by gettemplates.co
	Twitter: http://twitter.com/gettemplateco
	URL: http://gettemplates.co
-->
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>Traveler &mdash; Free Website Template, Free HTML5 Template by GetTemplates.co</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Free HTML5 Website Template by GetTemplates.co" />
	<meta name="keywords"
		content="free website templates, free html5, free template, free bootstrap, free website template, html5, css3, mobile first, responsive" />
	<meta name="author" content="GetTemplates.co" />

	<!-- Facebook and Twitter integration -->
	<meta property="og:title" content="" />
	<meta property="og:image" content="" />
	<meta property="og:url" content="" />
	<meta property="og:site_name" content="" />
	<meta property="og:description" content="" />
	<meta name="twitter:title" content="" />
	<meta name="twitter:image" content="" />
	<meta name="twitter:url" content="" />
	<meta name="twitter:card" content="" />

	<link href="https://fonts.googleapis.com/css?family=Lato:300,400,700" rel="stylesheet">

	<!-- Animate.css -->
	<link rel="stylesheet" href="css/animate.css">
	<!-- Icomoon Icon Fonts-->
	<link rel="stylesheet" href="css/icomoon.css">
	<!-- Themify Icons-->
	<link rel="stylesheet" href="css/themify-icons.css">
	<!-- Bootstrap  -->
	<link rel="stylesheet" href="css/bootstrap.css">

	<!-- Magnific Popup -->
	<link rel="stylesheet" href="css/magnific-popup.css">

	<!-- Magnific Popup -->
	<link rel="stylesheet" href="css/bootstrap-datepicker.min.css">

	<!-- Owl Carousel  -->
	<link rel="stylesheet" href="css/owl.carousel.min.css">
	<link rel="stylesheet" href="css/owl.theme.default.min.css">

	<!-- Theme style  -->
	<link rel="stylesheet" href="css/style.css">
    
    <!-- Pricing style -->
    <link rel="stylesheet" href="css/pricing.css">
    <link rel="stylesheet" href="css/modalForm.css">

	<!-- Modernizr JS -->
	<script src="js/modernizr-2.6.2.min.js"></script>
	<!-- FOR IE9 below -->
	<!--[if lt IE 9]>
	<script src="js/respond.min.js"></script>
	<![endif]-->
	

</head>

<body onload="resetForm()">

	<div class="gtco-loader"></div>

	<div id="page">


		<!-- <div class="page-inner"> -->
		<nav class="gtco-nav" role="navigation">
			<div class="gtco-container">

				<div class="row">
					<div class="col-sm-4 col-xs-12">
						<div id="gtco-logo"><a href="index.html">Go Shuttle Service <em>.</em></a></div>
					</div>
					<div class="col-xs-8 text-right menu-1">
						<ul>
							<li><a href="index">Home</a></li>
							<li><a href="reservations">Reservation</a></li>
							<li><a href="about-us.html">About Us</a></li>
							<!-- <li class="has-dropdown">
							<a href="#">Travel</a>
							<ul class="dropdown">
								<li><a href="#">Europe</a></li>
								<li><a href="#">Asia</a></li>
								<li><a href="#">America</a></li>
								<li><a href="#">Canada</a></li>
							</ul>
						</li> -->
							<li class="active"><a href="pricing">Pricing</a></li>
							<li><a href="contact.html">Contact</a></li>
						</ul>
					</div>
				</div>

			</div>
		</nav>

		<header id="gtco-header" class="gtco-cover gtco-cover-sm" role="banner"
			style="background-image: url(images/img_2.jpg)">
			<div class="overlay"></div>
			<div class="gtco-container">
				<div class="row">
					<div class="col-md-12 col-md-offset-0 text-center">
						<div class="row row-mt-15em">

							<div class="col-md-12 mt-text animate-box" data-animate-effect="fadeInUp">
								<h1>Plans for Everyone</h1>
							</div>

						</div>

					</div>
				</div>
			</div>
		</header>



		<div class="gtco-section border-bottom">
			<div class="gtco-container">
				<div class="row">
					<div class="col-md-8 col-md-offset-2 text-center gtco-heading">
						<h2>Prices For All Ages</h2>
						<p>Our fares come in different discounted prices for different ages. </p>
					</div>
				</div>
                
				<div class="row">
	            <div class="pricing-container">
	            <c:forEach items="${ageList}" var="ageGroup">
	                    
					<div class="price-display">
							<h2 class="pricing-plan">${ageGroup.getName()}</h2>
							<div class="price"><sup class="currency">$</sup><fmt:formatNumber value="${ageGroup.getPrice()}" 
																			minFractionDigits="2"
																			maxFractionDigits="2"/>
							</div>
							<c:choose>
								<c:when test="${ageGroup.getUpperAge() != 0 || ageGroup.getLowerAge() != 0}">
									<c:choose>
										<c:when test="${ageGroup.getUpperAge() == 0}">
											<p>Ages: <c:out value="${ageGroup.getLowerAge()}" />+</p>
										</c:when>
										<c:otherwise>
											<p>Ages <c:out value="${ageGroup.getLowerAge()}" />-<c:out value="${ageGroup.getUpperAge()}" /></p>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<p>Ages: All</p>
								</c:otherwise>
							</c:choose>
							<button class="btn btn-default btn-sm modal-select-button" onclick="openModal(this)" value="${ageGroup.getAgeId()}">Purchase</button>
					</div>
				</c:forEach>
						
                </div>
				</div>
			</div>
		</div>


		<footer id="gtco-footer" role="contentinfo">
			<div class="gtco-container">
				<div class="row row-p	b-md">

					<div class="col-md-4">
						<div class="gtco-widget">
							<h3>About Us</h3>
							<p>We are an affordable shuttle service providing transportation services between Greater
								Omaha and Lincoln
								since 2019. We have several pickup and drop of locations both in Omaha, NE and Lincoln,
								NE.</p>
						</div>
					</div>
					<div class="col-md-3 col-md-push-1">
						<div class="gtco-widget">
							<h3>Get In Touch</h3>
							<ul class="gtco-quick-contact">
								<li><a href="#"><i class="icon-phone"></i> +1 222 333 4455</a></li>
								<li><a href="#"><i class="icon-mail2"></i> info@goshuttleservice.com</a></li>
							</ul>
						</div>
					</div>
				</div>

				<div class="row copyright">
					<div class="col-md-12">
						<p class="pull-left">
							<small class="block">Copyright &copy; 2019 Go Shuttle Services, Inc. All Rights
								Reserved.</small>
							<small class="block">Designed by <a href="http://unomaha.edu/"
									target="_blank">unomaha.edu</a> Demo Images: <a href="http://unsplash.com/"
									target="_blank">Unsplash</a></small>
						</p>
						<p class="pull-right">
							<ul class="gtco-social-icons pull-right">
								<li><a href="#"><i class="icon-twitter"></i></a></li>
								<li><a href="#"><i class="icon-facebook"></i></a></li>
								<li><a href="#"><i class="icon-linkedin"></i></a></li>
								<li><a href="#"><i class="icon-dribbble"></i></a></li>
							</ul>
						</p>
					</div>
				</div>
			</div>
		</footer>
		<!-- </div> -->

	</div>
	
	<!-- The Modal -->
	<div id="modal-form" class="modal">
	
	  <!-- Modal content -->
	<%@ include file="purchaseFormContent/purchaseForm.jsp" %>
	
	</div>

	<div class="gototop js-top">
		<a href="#" class="js-gotop"><i class="icon-arrow-up"></i></a>
	</div>

	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- jQuery Easing -->
	<script src="js/jquery.easing.1.3.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Waypoints -->
	<script src="js/jquery.waypoints.min.js"></script>
	<!-- Carousel -->
	<script src="js/owl.carousel.min.js"></script>
	<!-- countTo -->
	<script src="js/jquery.countTo.js"></script>

	<!-- Stellar Parallax -->
	<script src="js/jquery.stellar.min.js"></script>

	<!-- Magnific Popup -->
	<script src="js/jquery.magnific-popup.min.js"></script>
	<script src="js/magnific-popup-options.js"></script>

	<!-- Datepicker -->
	<script src="js/bootstrap-datepicker.min.js"></script>


	<!-- Main -->
	<script src="js/main.js"></script>
	
	<script src="js/pricing.js"></script>
	

</body>

</html>