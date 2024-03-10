from datetime import datetime
import matplotlib.dates as md
import matplotlib.pyplot as plt
import matplotlib.dates as mlp_dates
import pandas as pd

plt.tight_layout()


def plot_daily(xdata, ydata):
    fig, ax = plt.subplots()
    plt.scatter(x=xdata, y=ydata)

    ax.xaxis.set_major_formatter(md.DateFormatter('%m/%d'))
    fig.autofmt_xdate()

    plt.title('Daily Feeding')
    plt.ylabel('Amount (Oz)')
    plt.xlabel('Dates')
    plt.show()


df = pd.read_csv("data/feeding.csv")
# df['Date'] = pd.to_datetime(df['Date'])

# print(df['Time'])
# plot_time(df['Time'], df['Amount'])

df["DateTime"] = df[['Date', 'Time']].apply(
    lambda row: datetime.strptime(' '.join(row.values.astype(str)), "%m/%d/%Y %H:%M"),
    axis=1)

df.sort_values("DateTime", inplace=True)

plt.plot_date(df["DateTime"], df["Amount"], linestyle='solid')
plt.gcf().autofmt_xdate()

# format x-axis
plt.gca().xaxis.set_major_formatter(mlp_dates.DateFormatter("%m/%d"))

plt.title('Daily Feeding')
plt.ylabel('Amount (Oz)')
plt.xlabel('Dates')
plt.show()

agg_df = df.groupby(['Date'])['Amount'].sum().reset_index(name='Total Amount')
agg_df["Date"] = pd.to_datetime(agg_df["Date"])
plot_daily(agg_df['Date'], agg_df['Total Amount'])
