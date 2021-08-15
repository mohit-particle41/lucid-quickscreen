# Lucid Quickscreen

Lucid Quickscreen is an Android-based application empowering retail pharmacy partners of HLT Hearing to administer OAE (otoacoustic emissions) hearing tests in the field, without assistance by an HLT technician.The application will interact with the Lucid OAE Scanner, immediately collecting and analyzing patient data in realtime, and then displaying results and actionable recommendations to the patient, while sending patient and application information back to Grand Central.

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone git@ssh.dev.azure.com:v3/lucidhearing/Lucid%20Quickscreen/Lucid%20Quickscreen
```

## Configuration
### Keystores:
Create `app/keystore.gradle` with the following info:
```gradle
ext.key_alias='...'
ext.key_password='...'
ext.store_password='...'
```
And place keystore under `app/keystores/` directory:
- `stage.keystore`


## Build variants
Use the Android Studio *Build Variants* button to choose between **release** and **staging** flavors combined with debug and release build types


## Generating signed APK
From Android Studio:
1. ***Build*** menu
2. ***Generate Signed APK...***
3. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*

## App Center deployment
1. Configure specific branch with build configuration
2. Upload keystore details with credentials
3. Select distribution group to notify users to download the build

## Contributing

1. Switch to master branch: `git checkout master`
2. Take pull master branch: `git pull origin master`
3. Create your feature branch: `git checkout -b <BRANCH_NAME>`
4. Commit your changes: `git commit -m 'Commit Message'`
5. Push to the branch: `git push origin <BRANCH_NAME>`
6. Submit a pull request.
