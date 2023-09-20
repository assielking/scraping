import java.io._
import org.jsoup.nodes._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.jsoup._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import scala.jdk.CollectionConverters._
import play.api.libs.json.Json
import scalaz.Leibniz.subst

object JsoupScraper {

  def main(args: Array[String]): Unit = {
    val doc = Jsoup.connect("https://www.goafricaonline.com/tg/annuaire/restaurants?page=1").get()

    // RESTAURANTS ADDRESS
    val restaurants = doc.select("#goafrica-main-container article ")

    // Créez un fichier CSV
    val csvFile = new File("restaurants.csv")
    val csvWriter = new PrintWriter(csvFile)

    // Utilisez un caractère de tabulation comme séparateur
    val separator = "\t"

    val browser = JsoupBrowser()

    // Écrivez l'en-tête du fichier CSV avec le séparateur de tabulation
    csvWriter.print("Nom" + separator + "Description" + separator + "Adresse" + separator + "Téléphone")

    // Récupérez les horaires depuis la première ligne
    val firstRestaurant = restaurants.asScala.headOption
    if (firstRestaurant.isDefined) {
      val popover = firstRestaurant.get.select("[data-floating-popover]").attr("data-floating-popover")

      if (!popover.isEmpty) {
        val popoverJson = Json.parse(popover)
        val popoverContent = (popoverJson \ "content").asOpt[String].getOrElse("")
        val docPopover = browser.parseString(popoverContent) >> element("table")

        for (row <- docPopover.select("tr")) {
          val header = row >> text("td:first-of-type")
          csvWriter.print(separator + header)
        }
      }
    }

    csvWriter.println() // Passer à une nouvelle ligne après l'en-tête

    // Utilisez une boucle for pour parcourir les dix premiers éléments de la liste restaurants
    for (restaurant <- restaurants.asScala.take(10)) {
      // Pour chaque élément restaurant, extrayez les informations suivantes

      // 1. Extraction du nom du restaurant (élément <a> avec la classe "stretched-link")
      val name = restaurant.select("a.stretched-link").text()
      // 2. Extraction de la description du restaurant (élément <div> avec les classes "text-gray-700" et "text-13")
      val description = restaurant.select("div.text-gray-700.text-13").text()
      // 3. Extraction de l'adresse du restaurant (élément <address>)
      val address = restaurant.select("address").text()
      // 4. Extraction du numéro de téléphone du restaurant (élément <a> avec un attribut href commençant par "tel:")
      val phone = restaurant.select("a[href^='tel:']").text()

      // Récupérer les horaires
      // Récupérez le contenu du popover
      val popover = restaurant.select("[data-floating-popover]").attr("data-floating-popover")

      // Vérifiez si le contenu du popover est présent
      if (!popover.isEmpty) {
        // Parsez le contenu du popover en tant qu'objet JSON
        val popoverJson = Json.parse(popover)

        // Récupérez le contenu du popover
        val popoverContent = (popoverJson \ "content").asOpt[String].getOrElse("")

        // Parsez le contenu du popover en tant que document Jsoup
        val docPopover = browser.parseString(popoverContent) >> element("table")

        // Initialisez une chaîne pour stocker les horaires
        val horaires = new StringBuilder

        for (row <- docPopover.select("tr")) {
          val horaire = row >> text("td:last-of-type")
          horaires.append(separator).append(horaire)
        }

        // Écrivez les données dans le fichier CSV
        csvWriter.println(s"$name$separator$description$separator$address$separator$phone$horaires")
      } else {
        // Écrivez les données dans le fichier CSV sans les horaires
        csvWriter.println(s"$name$separator$description$separator$address$separator$phone")
      }
    }

    // Fermez le fichier CSV
    csvWriter.close()
  }
}
