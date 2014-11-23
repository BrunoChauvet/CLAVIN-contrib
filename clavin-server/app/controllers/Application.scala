package controllers

import play.api._
import play.api.mvc._
import com.bericotech.clavin.GeoParser
import com.bericotech.clavin.GeoParserFactory
import com.bericotech.clavin.resolver.ResolvedLocation
import scala.collection.JavaConverters._
import play.api.libs.json.Json
import play.api.libs.json.JsObject
import play.api.libs.json.JsArray
import scala.collection.mutable.ListBuffer
import com.typesafe.config._

object Application extends Controller {
  
  def index() = Action { request =>
	    val conf = ConfigFactory.load()
    	var parser:GeoParser = GeoParserFactory.getDefault(conf.getString("clavin.index"))
    	val body: AnyContent = request.body
    	val textBody: Option[String] = body.asText
    	  
    	textBody.map { text =>   	    	 
    	// Parse location names in the text into geographic entities
		val resolvedLocations = parser.parse(text)
		
		val rl = resolvedLocations.asScala
    	
		val locs = new ListBuffer[JsObject]()
		for( r <- rl){
			var jl = Json.obj(
				     "geonameID" -> r.getGeoname().getGeonameID(),
					 "name" -> r.getGeoname().getName(),
					 "locationText" -> r.getLocation().getText(),
                     "countryName" -> r.getGeoname().getPrimaryCountryName(),
                     "population" -> r.getGeoname().getPopulation(),
                     "admin1Code" -> r.getGeoname().getAdmin1Code(),
					 "locationPosition" -> r.getLocation().getPosition(),
					 // "fuzzy" -> r.getFuzzy(),
					 "confidence" -> r.getConfidence(),
					 "latitude" -> r.getGeoname().getLatitude(),
					 "longitude" -> r.getGeoname().getLongitude()
			)
			locs.append(jl)
		}
				
    	val results = Json.obj(
			"version" -> "1.0.0",
			"locations" -> JsArray(locs)) 	
			Ok(Json.toJson(results))
    		
    		}.getOrElse {
    			BadRequest("Expecting text/plain request body")  
    	}
  	}
}
