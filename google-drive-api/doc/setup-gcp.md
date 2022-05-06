#Google Cloud Platform Project Setup

##Create New GCP Project
![Create GCP Project](screenshots/setup-gcp-001.png)
  
  
##Enable Drive API
![Enable API](screenshots/setup-gcp-002.png)
  
![Enable API](screenshots/setup-gcp-003.png)
  
![Enable API](screenshots/setup-gcp-004.png)
  
  
##Configure OAuth consent screen
![Configure](screenshots/setup-gcp-005.png)
  
![Configure](screenshots/setup-gcp-006.png)
  
Fill out **App Information** screen, and click **Save and Continue**.  
  
In the **Scopes** screen, select **drive.file** and **drive.appdata** scopes, and click **Update**.  
  
![Configure](screenshots/setup-gcp-007.png)
  
In **Test Users** screen, click **Save and Continue**.  
  
In the **Summary** screen, click **Back to Dashboard**.  
  

##Publish the Application  
In the **OAuth Consent Screen**, click **Publish**.
  
![Configure](screenshots/setup-gcp-008.png)  

##Create Credentials for the Application
From the **Credentials** page, click **Create Credentials**.  
  
Then select **OAuth client ID**.  

![Configure](screenshots/setup-gcp-009.png)  
  
Select **Desktop Application** type, and click **Create**.  

![Configure](screenshots/setup-gcp-010.png)  

Click **Download JSON** to get the credentials file to put in your project.
  
![Configure](screenshots/setup-gcp-011.png)  
