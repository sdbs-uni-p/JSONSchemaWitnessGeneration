# Remove cancelled runs from dataset

gen_df <- function(in_path, out_path) {
  df  <- read.csv(in_path, sep=",")
  clean_df <- df[(is.na(df$cancelledAt) | df$cancelledAt==""),]
  write.csv(clean_df,out_path, row.names = FALSE)
}

gen_df('data/results.csv', 'data/clean_data.csv')


