require('dotenv/config')

export function fetchPoints(q){

 	let query = process.env.REACT_APP_BACKEND_API_URL;
 
	if(q){
		query += 'ping?size=' + q;
		return fetch(query)
    	   .then((res) => res.json())
	}
	
	// let res = '[{"weight":1245.0,"lat":14.25,"lng":45.4455},{"weight":546.0,"lat":24.2465,"lng":25.124},{"weight":789.0,"lat":44.2536,"lng":75.5643}]'
	// return JSON.parse(res);
	
}		