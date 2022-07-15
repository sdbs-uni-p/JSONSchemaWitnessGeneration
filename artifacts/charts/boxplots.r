library(ggplot2)
library(extrafontdb)
library(extrafont)
library(tidyr)
library(dplyr, warn.conflicts=FALSE)
library(scales)
source('config.r')
df  <- read.csv('data/realWorldSchemas/clean_data.csv')

# We divide the processing steps into three groups as defined here
grp.trans = c("parsing","extractSchema","X2Full","X2Witness","notElim","merge1",
                    "groupize","separation","expansion")
grp.dnf = c("preparation","merge3")
grp.gen = c("initGEnv","genWitness")

# Add new columns that contain the sum of the groups defined above
df <- df %>% mutate(trans = rowSums(select(.,all_of(grp.trans))))
df <- df %>% mutate(prepDnf = rowSums(select(.,all_of(grp.dnf))))
df <- df %>% mutate(gen = rowSums(select(.,all_of(grp.gen))))

# Columns to include in the boxplot
clmns =  c("trans", "prepDnf","gen")

# Names of labels in the boxplot's legend. Mapping is done by order, i.e. the nth element 
# in clmns is assigned the name of the nth elemen int clmns.labels
clmns.labels =  c("Trans", "P&DNF","Gen")

# Create tidy dataframe
df.pivot <- df %>% pivot_longer(all_of(clmns), names_to = "procStep",values_to = "value")
df.tidy <- select(df.pivot, "procStep", "value")
df.tidy$procStep <- factor(df.tidy$procStep , levels=clmns)

quantiles <- function(x) {
  r <- quantile(x, probs = c(0.05, 0.25, 0.5, 0.75, 0.95))
  names(r) <- c("ymin", "lower", "middle", "upper", "ymax")
  r
}

outlier <- function(x) {
  subset(x, x < quantile(x, 0.05) | quantile(x, 0.95) < x)
}

# Plot data
p.base <- ggplot(df.tidy, aes(x=procStep, y=value, fill=procStep)) + 
  stat_summary(fun.data=quantiles, geom="boxplot", width=0.75) +
  stat_summary(fun.y=outlier, geom="point", alpha=0.2) +
  stat_summary(fun.data=quantiles, geom = "errorbar", color="black", width=0.5) + 
  scale_y_continuous(trans = "pseudo_log",
                     breaks = 10^(0:7),
                     labels = trans_format('log10', math_format(10^.x)),
                     limits = c(0, 10^6.45))  +
  labs(x = "Algorithm Phases", y = "Time [ms]",  fill = "Processing Step") +
  scale_x_discrete(labels = clmns.labels) + 
  theme(axis.title=element_text(size=text_size, family=text_family),
        axis.title.x=element_text(margin = margin(t = 0.25, r = 0, b = 0, l = 0, unit="cm")),
        axis.text=element_text(size=text_size, family=text_family),
        plot.title = element_text(hjust = 0.5),
        plot.caption = element_text(hjust = 0.5),
        legend.position = "none",
        plot.margin = margin(b=0.21, unit="cm"))

p.base
ggsave(paste("charts/boxplot",file_ending,sep=""), height=4.263, width=4.2, device=cairo_pdf)
