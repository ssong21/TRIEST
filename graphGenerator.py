import matplotlib.pyplot as plt
import numpy as np

check = 500
test = 0
parse = 'Base'
while test < 2:
	if test == 1:
		parse = 'Impr'
		check = 500
	while check < 5000:
		data = [[] for x in range(198050)] #creating list size 508837 of lists, exact number TBD 
		for x in range(20): #go through the 20 files
			fileName = 'testOut' + parse + str(check) + 'RUN' #change Impr to Base and vise versa
			combinedName = fileName + str(x) + '.txt' #appending of file name with number
			y = 0 #used to choose which list to add integer on line to
			print(combinedName)
			with open(combinedName) as file: #go through the chosen file
				for line in file:
						data[y].append(int(line.strip())) #strips new line character and appends to a list in the list of lists
						y += 1
		#at this stage, data is a list of list of 20 elements at every timestep. 
		minData = []
		maxData = []
		medianData = []
		lowPData = []
		highPData = []
		for list in data:
			minData.append(np.min(list))
			maxData.append(np.max(list))
			medianData.append(np.median(list))
			lowPData.append(np.percentile(list,25))
			highPData.append(np.percentile(list,75))
		#at this stage, we have 5 arrays, each containing the min/max/median/25/75 percentile values for every time step
		#next is plotting all 5 lists on the same plot, as below
		plt.figure()
		lineMin = plt.plot(minData, color = 'red', label = 'Minimum')
		lineMax = plt.plot(medianData, color = 'blue', label = 'Median')
		lineMed = plt.plot(maxData, color = 'green', label = 'Maximum')
		lineLowP = plt.plot(lowPData, color = 'purple', label = 'First Quartile')
		lineHighP = plt.plot(highPData, color = 'orange', label = 'Third Quartile')
		ax = plt.gca()
		box = ax.get_position()
		ax.set_position([box.x0,box.y0,box.width*0.8,box.height])
		plt.legend(loc = 'center left', bbox_to_anchor=(1,0.5))
		plt.xlabel('Time Step')
		plt.ylabel('Triangle Estimate')
		if check == 0:
			plt.title('TRIEST ' + parse + ' (5000): Triangle Estimate vs. Time Step') #change Impr to Base and vise versa
		else:
			plt.title('TRIEST ' + parse + ' (' + str(check) + '): Triangle Estimate vs. Time Step') #change Impr to Base and vise versa
		plt.grid(True)
		check += 500
	test+=1
plt.show()
