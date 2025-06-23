# SIMTEST Automation Project
# SIMTEST Automation Project

This project automates the reservation process for virtual numbers using the SIMTEST platform.
Countries that have numbers for automation:
{Bahrain , United Arab Emirates , Oman , Senegal , Turkey , Spain , Portugal}.

## 🔧 Tools & Technologies

- Java 17
- Selenium WebDriver
- TestNG
- Maven
- Page Object Model (POM)
- IntelliJ IDEA

## 🚀 How to Run

1. Open the project in IntelliJ IDEA
2. Make sure Java 17 and Maven are properly installed
3. Run the desired TestNG XML file from `src/test` folder "TC_SIMTestReservation"
4. Make sure to add the country that you need to test it's naumber like "Bahrain" ==> "reserve.selectCountry("Bahrain")"
5. Make sure to add the index of the number you want to reserve ==> "reserve.chooseNumberByIndex(0);"
6. Make sure to add the index of the free slot that you want to reserve ==> "reserve.chooseFreeSlotByIndex(2);"
7. Run the test file.

## 📁 Project Structure