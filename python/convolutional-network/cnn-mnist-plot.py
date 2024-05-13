import matplotlib.pyplot as plt
import importlib


cnn_module = importlib.import_module("cnn-mnist-model")
stats_filepath = cnn_module.default_train_stats_path
stats = cnn_module.load_stats(stats_filepath)

x_axis = list(range(1, stats.num_epochs+1))

plt.tight_layout()
plt.title("Loss")
plt.plot(x_axis, stats.losses)
plt.show()

plt.cla()
plt.title("Accuracy")
plt.plot(x_axis, stats.accuracies)
plt.show()
