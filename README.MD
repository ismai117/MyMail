<h1>MyMail</h1></br>

A Kotlin Multiplatform Application for seamless Email and SMS messaging, using the KSend library I built which utilises SendGrid/Twilio API's.

# Platforms 

- Android 
- Ios 
- Desktop
- Web
</br>

# Setup

In local.properties file add the following Placeholders

```
API_KEY=
ACCOUNTSID=
AUTHTOKEN=

SENDER_EMAIL_ADDRESS=
SENDER_PHONE_NUMBER=

GEMINI_API_KEY=
```
</br>

To utilize the functionalities of this library for sending emails and SMS using SendGrid APIs, please follow these steps:

- Sign up for SendGrid: Head over to SendGrid's website and sign up for an account to obtain your API key. Once signed up, copy your API key and paste it into the API_KEY placeholder in the local.properties file.
- Configure Sender Email Address: Set your sender email address in the SENDER_EMAIL_ADDRESS placeholder in the local.properties file.
- Sign up for Twilio: Similarly, visit Twilio's website and sign up for an account to acquire your Account SID and Auth Token for SMS functionalities. Copy these credentials and paste them into the ACCOUNTSID and AUTHTOKEN placeholders respectively in the local.properties file.
- Configure Sender Phone Number: Set your sender phone number in the SENDER_PHONE_NUMBER placeholder in the local.properties file.
- Gemini API Key (Optional): If you plan to use the Gemini API, ensure you have obtained the API key and paste it into the GEMINI_API_KEY placeholder in the local.properties file.

After completing these steps, you'll be ready to seamlessly send emails and SMS using the provided library, leveraging SendGrid for email functionality and Twilio for SMS.

<pre>
<h2 align="center">Android</h2>
</pre>
<p align="center">
  <img src="readme_images/android/Email_Screen_Dark_Mode.png" height="500px">
</p>

<pre>
<h2 align="center">Ios</h2>
</pre>
<p align="center">
  <img src="readme_images/ios/Email_Screen_Dark_Mode.png" height="500px">
</p></br>

<pre>
<h2 align="center">Desktop</h2>
</pre>
<p align="center">
  <img src="readme_images/desktop/Email_Screen_Dark_Mode.png" height="500px">
</p>

<pre>
<h2 align="center">Web</h2>
</pre>
<p align="center">
  <img src="readme_images/web/Email_Screen_Dark_Mode.png" height="500px">
</p>
