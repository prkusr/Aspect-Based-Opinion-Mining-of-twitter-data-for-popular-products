require('dotenv/config')

export function fetchPoints(q){

 // 	let query = process.env.REACT_APP_BACKEND_API_URL;
 
	// if(q){
	// 	query += 'ping?size=' + q;
	// 	return fetch(query)
 //    	   .then((res) => res.json())
	// }
	
	// let res = '[{"weight":1245.0,"lat":14.25,"lng":45.4455},{"weight":546.0,"lat":24.2465,"lng":25.124},{"weight":789.0,"lat":44.2536,"lng":75.5643}]'
	// return JSON.parse(res);
	return test();
}		


function test(){
	let resp =  `
		[{
		"category": "camera",
		"info": [{
				"words": [
					"shot"
				],
				"sentiment": 2,
				"id": 990433011953958900,
				"text": "Great shot of the #moonbow #CumberlandFalls #Samsung #mycameraisbetterthanyours #promode @kywx @WKYT  @WYMT https://t.co/y9zO1CTdBg"

			},
			{
				"words": [
					"gon"
				],
				"sentiment": 0,
				"id": 990445031231287300,
				"text": "Work is gonna suck cus people don't know how to take care of things.. Another broken Samsung TV.."
			}
		]
	},
	{
		"category": "Functionality",
		"info": [{
				"words": [
					"shot"
				],
				"sentiment": 2,
				"id": 9904330119539589,
				"text": "Great shot of the #moonbow #CumberlandFalls #Samsung #mycameraisbetterthanyours #promode @kywx @WKYT  @WYMT https://t.co/y9zO1CTdBg"

			},
			{
				"words": [
					"gon"
				],
				"sentiment": 0,
				"id": 9904450312312873,
				"text": "Work is gonna suck cus people don't know how to take care of things.. Another broken Samsung TV.."
			}
		]
	}
]	`

return JSON.parse(resp);
}