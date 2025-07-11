from typing import Any
import httpx
from mcp.server.fastmcp import FastMCP

mcp = FastMCP("weather")

WEATHER_SERVICE_BASE_API = "https://api.weather.gov"
USER_AGENT = "weather-app/1.0"

async def make_service_request(url: str) -> dict[str, Any] | None:
	headers = {
		"User-Agent": USER_AGENT,
		"Accept": "application/geo+json"
	}
	
	async with httpx.AsyncClient() as client:
		try:
			response = await client.get(url, headers=headers, timeout=30.0)
			response.raise_for_status()
			return response.json()
		except Exception as e:
			raise Exception(str(e))
	
def format_alert(feature: dict) -> str:
	props = feature["properties"]
	return f"""
	Event: {props.get('event', 'Unknown')}
	Area: {props.get('areaDesc', 'Unknown')}
	Severity: {props.get('severity', 'Unknown')}
	Description: {props.get('description', 'No description available')}
	Instructions: {props.get('instruction', 'No specific instructions provided')}
	"""

@mcp.tool()
def ping() -> str:
	"""A simple tool to check if the server is alive."""
	return "pong"

@mcp.tool()
async def get_alert(state: str) -> str:
	"""
	Get active weather alerts for a US state.

	Args:
		state: The two-letter state code (e.g., 'CA' for California).

	Returns:
		A formatted string with the current weather alerts for the state.
	"""
	url = f"{WEATHER_SERVICE_BASE_API}/alerts/active/area/{state}"

	try:
		data = await make_service_request(url)
	except Exception as e:
		raise e

	if not data or "features" not in data:
		return "No alerts"
	
	if not data["features"]:
		return "No active alerts for this state"
	
	alerts = [format_alert(feature) for feature in data["features"]]
	return "\n -*- \n".join(alerts)

@mcp.tool()
async def get_forecast(latitude: float, longitude: float) -> str:
	"""
	Get the weather forecast for a given latitude and longitude.

	Args:
		latitude: The latitude of the location.
		longitude: The longitude of the location.

	Returns:
		A formatted string with the weather forecast for the next 5 periods (Each period is half a day)
	"""

	points_url = f"{WEATHER_SERVICE_BASE_API}/points/{latitude},{longitude}"
	try:
		points_data = await make_service_request(points_url)
	except Exception as e:
		raise e

	if not points_data:
		return "Cannot fetch forecast for this location"
	
	forecast_url = points_data["properties"]["forecast"]
	try:
		forecast_data = await make_service_request(forecast_url)
	except Exception as e:
		raise e

	if not forecast_url:
		return "Unable to fetch detailed forecast"
		
	periods = forecast_data["properties"]["periods"]
	forecasts = []
	for period in periods[:5]:  # Only show next 5 periods
		forecast = f"""
		{period['name']}:
		Temperature: {period['temperature']}°{period['temperatureUnit']}
		Wind: {period['windSpeed']} {period['windDirection']}
		Forecast: {period['detailedForecast']}
		"""
		forecasts.append(forecast)

	return "\n---\n".join(forecasts)

if __name__ == "__main__":
	mcp.run(transport='sse')
