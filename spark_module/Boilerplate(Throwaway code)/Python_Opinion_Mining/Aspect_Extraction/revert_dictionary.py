import cat_dict
import codecs
def revert_dict():
	dictionary = cat_dict.category_dict()
	set_elements = set()
	for cat in dictionary:
		for element in dictionary[cat]:
			set_elements.add(element)
	revert_dict = dict()
	for element in set_elements:
		for cat in dictionary:
			if element in dictionary[cat]:
				if element not in revert_dict:
					revert_dict[element] = []
				revert_dict[element].append(cat)
	
	fp = codecs.open('revert_dict.txt','w', encoding = 'utf-8')
	fp.write(str(revert_dict))
	fp.close()
	return revert_dict
def java_format():	
	total = []
	# wordToCategories.put("word", Arrays.asList("cat1", "cat2"));
	revert = revert_dict()
	fp = open("dictionary.txt","w")
	
	for element in revert:
		t = 'wordToCategories.put("'+element+'", '
		# print type(t)
		if len(revert[element]) == 1:
			t += 'Collections.singletonList('
		else:
			t += 'Arrays.asList('
		for a in revert[element]:
			print a

			# print(type(a))
			t += '"'+a+'", '
		t = t.strip()
		t = t.strip(',')
		t += '));'
		# print t
		fp.write(t+'\n')
		total.append(t)


	# inv_map = {v: k for k, v in dictionary.iteritems()}


java_format()