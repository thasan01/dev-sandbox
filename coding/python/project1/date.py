
from datetime import datetime

date_string = '2/18/2024 17:30'
d1 = datetime.strptime(date_string, "%m/%d/%Y %H:%M")

print('All possible datetime attributes:')
print(dir(d1))

print(d1.time())
print(d1.strftime('%Y-%m-%d %I:%M %p'))
