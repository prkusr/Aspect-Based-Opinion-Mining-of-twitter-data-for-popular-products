export function fetchPoints(){

	return fetch(`http://localhost:7000/ping`)
    	   .then((res) => res.json())

	// return [{"weight":1245.0,"lat":14.25,"lng":45.4455},{"weight":546.0,"lat":24.2465,"lng":25.124},{"weight":789.0,"lat":44.2536,"lng":75.5643}]
}