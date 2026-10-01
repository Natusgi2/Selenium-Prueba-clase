package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://localhost:3001";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(description = "Validar mensaje de error o bloqueo ante credenciales invalidas")
    public void validarCredencialesInvalidas() {
        driver.get(BASE_URL);

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
        WebElement passwordInput = driver.findElement(By.xpath("//input[@type='password']"));
        WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(),'Ingresar')]"));

        emailInput.sendKeys("usuario_erroneo@test.com");
        passwordInput.sendKeys("clave_invalida_123");
        submitButton.click();

        // Verifica que permanezca en la vista o muestre el mensaje de alerta
        WebElement vistaLogin = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Iniciar Sesión') or contains(@class,'alert')]")));
        Assert.assertTrue(vistaLogin.isDisplayed(), "Debe mantenerse en el formulario o mostrar alerta.");
    }

    @Test(description = "Validar navegacion fluida y cambio de vista entre Login y Registro")
    public void validarNavegacionRegistroYLogin() {
        driver.get(BASE_URL);

        // 1. Verificar carga del formulario de login
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
        Assert.assertTrue(emailInput.isDisplayed());

        // 2. Navegar hacia la vista de Registro usando el boton del Navbar o el enlace
        WebElement registroBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Registro')] | //a[contains(text(),'Regístrate')]")));
        registroBtn.click();

        // 3. Confirmar que cambio a la vista de Registro
        WebElement tituloRegistro = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Registro') or contains(text(),'Crear')]")));
        Assert.assertTrue(tituloRegistro.isDisplayed(), "Debe navegar al formulario de registro.");

        // 4. Regresar a Login
        WebElement loginNavBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(),'Login') or contains(text(),'Iniciar Sesión')]")));
        loginNavBtn.click();

        // 5. Confirmar retorno exitoso al login
        WebElement inputRetorno = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='email']")));
        Assert.assertTrue(inputRetorno.isDisplayed(), "Debe regresar al login.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}