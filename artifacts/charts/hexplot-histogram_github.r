library(ggplot2)
library(ggrepel)
library(ggExtra)
library(extrafontdb)
library(extrafont)
library(tidyr)
library(dplyr, warn.conflicts=FALSE)
library(scales)

source('config.r')
df <- read.csv('data/github/clean_data.csv')
df_timeout <- read.csv('data/github/timeout.csv')

hexplot_fix <- function(col.low="#132B43", col.high="#56B1F7", col.med="red", col.inf="red", y_title="") {
  size_median <- median(df$inSize)
  time_median <- median(df$totalTime)
  
  if (nrow(df_timeout) > 0) {
    timeouts <- geom_point(data=df_timeout, aes(x=inSize, y=Inf), colour=col.inf)
    average_timeout_x <- mean(df_timeout$inSize)
    num_rows <- nrow(df_timeout)
    label_text <- paste("Timeouts", paste("(", num_rows, ")", sep = ""), sep = " ")
    timeout_txt <- geom_text(aes(x=average_timeout_x, label=label_text, y=10^6.5), nudge_y=0.7, nudge_x=-0.3, vjust=0,
              colour=col.inf, family=text_family, check_overlap = TRUE, size=7)
  } else {
    timeouts <- NULL
    timeout_txt <- NULL
  }
  
  plot <- ggplot(data=df, aes(x = inSize, y=totalTime)) + 
    geom_point(alpha=0) + timeouts + timeout_txt +
    geom_hex(bins=256, binwidth = c(.15, .15)) +
    geom_vline(xintercept=size_median, size=.5, color=col.med, linetype = "dashed") +
    geom_text(aes(x=size_median, label="Median", y=10^6), nudge_x=-0.4, nudge_y=-0.2, colour=col.med, 
              angle=90, family=text_family, check_overlap = TRUE, size=7) +
    geom_hline(yintercept=time_median, size=.5, color=col.med, linetype = "dashed") + 
    geom_text(aes(x=10^5.3, label="Median", y=time_median), nudge_x=0.1, nudge_y=0.6, colour=col.med, 
              family=text_family, check_overlap = TRUE, size=7) +
    labs(x = "File Size [Bytes]", y = y_title) +
    scale_fill_gradient(name="Count", low=col.low, high=col.high) +
    scale_y_continuous(trans = "pseudo_log", breaks = 10^(0:7),
                       labels = trans_format('log10', math_format(10^.x)),
                       #oob = squish_infinite,
                       limits = c(0, 10^7))  +
    scale_x_continuous(trans = "pseudo_log", breaks = 10^(0:7),
                       labels = trans_format('log10', math_format(10^.x)),
                       limits = c(0, 10^7))  +
    theme(axis.title=element_text(size=text_size, family=text_family),
          axis.text=element_text(size=text_size, family=text_family),
          legend.position = c(.085,.72),
          legend.key.size = unit(0.65, 'cm'),
          legend.background = element_rect(fill="white",
                                           size=0.5, linetype="solid", 
                                           colour ="black"),
          legend.text = element_text(size=text_size-2, family=text_family),
          legend.title =element_text(size=text_size, family=text_family),
          legend.spacing.y = unit(0.1, 'cm'))
  plot <- ggMarginal(plot, type = "histogram", fill="transparent", size=10)
  return(plot)
}

plot <- hexplot_fix(col.low="#56B1F7", col.high="#132B43", y_title="Time [ms]")
ggsave(paste("output/hexplot_and_histograms_github_timeout_fix",file_ending,sep=""), plot=plot, height=5, width=8.5, device=cairo_pdf)
