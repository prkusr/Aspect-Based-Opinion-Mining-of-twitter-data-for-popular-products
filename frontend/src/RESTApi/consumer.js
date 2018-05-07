require('dotenv/config');

export function fetchPoints(q) {

    let query = process.env.REACT_APP_BACKEND_API_URL;
    if (q) {
        query += 'opinion?product=' + q;
        return fetch(query)
            .then((res) => res.json())
    }
    // return test();
}


function test() {
    let resp = `
		[{
			"searchString": "Samsung",
			"isOpinion": true,
			"totalTweets": 10,
			"aspects": [{
					"category": "camera",
					"words": ["shot"],
					"sentiment": -1
				},
				{
					"category": "screen",
					"words": ["shit", "Horrible"],
					"sentiment": 0
				}
			],

			"location": [-89.57151, 36.497129],
			"id": 990433011953958912,
			"text": "Great shot of the #moonbow #CumberlandFalls #Samsung #mycameraisbetterthanyours #promode @kywx @WKYT  @WYMT https://t.co/y9zO1CTdBg"
		}, {
			"searchString": "Samsung",
			"isOpinion": false,
			"totalTweets": 10,
			"aspects": [],

			"location": [-89.57151, 37.497129],
			"id": 990433011953958912,
			"text": "Parts of Fun"
		}, {
			"searchString": "Samsung",
			"isOpinion": true,
			"totalTweets": 10,
			"aspects": [{
				"category": "Face",
				"words": ["Shit", "Chii"],
				"sentiment": -4
			},{
					"category": "camera",
					"words": ["shot"],
					"sentiment": 1
				}],

			"location": [-89.57151, 35.497129],
			"id": 990433011953958912,
			"text": "Face is just a mask"
		}]	`;

    return JSON.parse(resp);
}